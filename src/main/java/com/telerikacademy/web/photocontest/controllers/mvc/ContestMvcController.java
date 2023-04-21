package com.telerikacademy.web.photocontest.controllers.mvc;

import com.sun.net.httpserver.HttpsServer;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper;
import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dto.*;
import com.telerikacademy.web.photocontest.models.validations.CreatePhotoViaContestGroup;
import com.telerikacademy.web.photocontest.models.validations.CreateValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.EnlistUserValidationGroup;
import com.telerikacademy.web.photocontest.models.validations.UpdateValidationGroup;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import com.telerikacademy.web.photocontest.services.contracts.ContestServices;
import com.telerikacademy.web.photocontest.services.contracts.PhotoServices;
import com.telerikacademy.web.photocontest.services.contracts.UserServices;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.DateTimeException;
import java.util.List;
import java.util.Map;

import static com.telerikacademy.web.photocontest.helpers.DateTimeFormatHelper.getPhaseRemainingTime;
import static com.telerikacademy.web.photocontest.helpers.FilterAndSortingHelper.getResult;

@Controller
@RequestMapping("/contests")
@AllArgsConstructor
@Log4j2
public class ContestMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final CategoryServices categoryServices;
    private final ContestServices contestServices;
    private final PhotoServices photoServices;
    private final UserServices userServices;
    private final ModelMapper modelMapper;

    @ModelAttribute("categories")
    public Iterable<Category> populateCategories() {
        return categoryServices.findAll();
    }

    @ModelAttribute("photos")
    public List<Photo> populatePhotos() {
        return photoServices.getAll();
    }

    @ModelAttribute("juries")
    public List<User> populateJuries() {
        return userServices.getUsersWithJuryPermission();
    }

    @GetMapping
    public String showAllContests(@PageableDefault(size = 9, sort = "id") Pageable pageable,
                                  @RequestParam(required = false) Map<String, String> parameters,
                                  Model model, HttpSession session, HttpServletRequest request) {
        try {
            authenticationHelper.tryGetUser(session);
            FilterAndSortingHelper.Result result = getResult(parameters, pageable);
            Page<Contest> contestPage = contestServices.filter(result.title(), result.categoryName(), result.isInvitational(), result.isFinished(),
                    result.phase(), result.now(), result.pageable());
            List<ContestResponseDto> list = contestPage.stream().map(modelMapper::objectToDto).toList();
            Page<ContestResponseDto> page = new PageImpl<>(list, contestPage.getPageable(), contestPage.getTotalElements());

            String sortParams = pageable.getSort().toString().replace(": ASC", "");
            WebUtils.setSessionAttribute(request, "filterParams", parameters);
            WebUtils.setSessionAttribute(request, "sortParams", sortParams);

            model.addAttribute("contests", page);
            return "ContestsView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String showContest(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            Contest contest = contestServices.findById(id);
            ContestResponseDto contestResponseDto = modelMapper.objectToDto(contest);
            String phase1Ends = getPhaseRemainingTime(contest.getPhase1());
            String phase2Ends = getPhaseRemainingTime(contest.getPhase2());
            model.addAttribute("contest", contestResponseDto);
            model.addAttribute("phase1Ends", phase1Ends);
            model.addAttribute("phase2Ends", phase2Ends);
            return "ContestView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/create")
    public String showCreateContest(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            model.addAttribute("contest", new ContestDto());
            return "ContestCreateView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/create")
    public String createContest(@Validated(CreateValidationGroup.class) @ModelAttribute("contest") ContestDto contestDto,
                                BindingResult bindingResult, Model model,
                                HttpSession session, HttpServletRequest request) {

        if (bindingResult.hasErrors()) return "ContestCreateView";

        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            Contest contest = modelMapper.dtoToObject(contestDto);
            contestServices.create(contest, user, contestDto.getCoverPhotoUpload());
            return "redirect:/contests/" + contest.getId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("title", "duplicate_title", e.getMessage());
            return "ContestCreateView";
        } catch (DateTimeException e) {
            if (e.getMessage().startsWith("Phase 1")) {
                bindingResult.rejectValue("phase1", "phase1_invalid", e.getMessage());
            } if (e.getMessage().startsWith("Phase 2")) {
                bindingResult.rejectValue("phase2", "phase2_invalid", e.getMessage());
            }
            return "ContestCreateView";
        } catch (FileUploadException e) {
            bindingResult.rejectValue("file", "file_invalid", e.getMessage());
            return "ContestCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showUpdateContest(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            ContestDto contestDto = modelMapper.objectToDto(id);
            model.addAttribute("contest", contestDto);
            return "ContestUpdateView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateContest(@PathVariable Long id, @Validated(UpdateValidationGroup.class) @ModelAttribute("contest") ContestDto contestDto,
                                    BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) return "ContestUpdateView";

        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            Contest contest = modelMapper.dtoToObject(id, contestDto);
            contestServices.update(contest, user, contestDto.getCoverPhotoUpload());
            return "redirect:/contests/" + id;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("title", "duplicate_title", e.getMessage());
            return "ContestUpdateView";
        } catch (FileUploadException e) {
            bindingResult.rejectValue("file", "file_invalid", e.getMessage());
            return "ContestUpdateView";
        }
    }

    @GetMapping("/{id}/join")
    public String joinContest(@PathVariable Long id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Contest contest = contestServices.findById(id);
            Contest joinedContest = contestServices.join(contest, user);
            model.addAttribute("contest", joinedContest);
            return "redirect:/contests/" + id;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (EntityDuplicateException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping({
            "/{id}/add-jury",
            "/{id}/add-participant"
    })
    public String enlistUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            model.addAttribute("user", new UserDto());
            return "ContestEnlistView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping({
            "/{id}/add-jury",
            "/{id}/add-participant"
    })
    public String enlistUser(@PathVariable Long id, @Validated(EnlistUserValidationGroup.class) @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request) {

        if (bindingResult.hasErrors()) return "ContestEnlistView";

        try {
            User organizer = authenticationHelper.tryGetOrganizer(session);
            Contest contest = contestServices.findById(id);
            Contest updatedContest;
            if (request.getRequestURI().endsWith("/add-jury")) {
                updatedContest = contestServices.addJury(contest, organizer, userDto.getUsername());
            } else if (request.getRequestURI().endsWith("/add-participant")) {
                updatedContest = contestServices.addParticipant(contest, organizer, userDto.getUsername());
            } else {
                model.addAttribute("error", "Invalid Endpoint!");
                return "NotFoundView";
            }
            model.addAttribute("contest", updatedContest);
            return "redirect:/contests/" + id;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("username",  "user_not_found", e.getMessage());
            return "ContestEnlistView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "duplicate_user", e.getMessage());
            return "ContestEnlistView";
        } catch (UnauthorizedOperationException e) {
            bindingResult.rejectValue("username", "user_cannot_participate", e.getMessage());
            return "ContestEnlistView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteContest(@PathVariable Long id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            contestServices.deleteById(id, user);
            return "redirect:/contests";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{contestId}/photos/{photoId}")
    public String showPhoto(@PathVariable Long contestId, @PathVariable Long photoId,
                            HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetUser(session);
            Photo photo = photoServices.getPhotoByContestId(photoId, contestId);
            model.addAttribute("photo", photo);
            return "PhotoViewTest";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/photos/new")
    public String showCreatePhotoPage(@PathVariable Long id, Model model,
                                      HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            contestServices.findById(id);
            model.addAttribute("photo", new PhotoDto());
            return "PhotoCreateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/photos/new")
    public String createPhoto(@PathVariable Long id, Model model,
                              HttpSession session,
                              @Validated(CreatePhotoViaContestGroup.class)
                                  @ModelAttribute("photo") PhotoDto photoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "PhotoCreateView";
        try {
            User user = authenticationHelper.tryGetUser(session);
            Contest contest = contestServices.findById(id);
            Photo photo = modelMapper.dtoToObject(photoDto);
            photo.setPostedOn(contest);
            photo.setUserCreated(user);
            photoServices.create(photo, photoDto.getFile());
            return "redirect:/contests/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("title", "photo_exists", e.getMessage());
            return "PhotoCreateView";
        } catch (FileUploadException e) {
            bindingResult.rejectValue("file", "file_invalid", e.getMessage());
            return "PhotoCreateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("{id}/photos/{photoId}/delete")
    public String deletePhoto(@PathVariable Long id,
                              @PathVariable Long photoId,
                              Model model,
                              HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Contest contest = contestServices.findById(id);
            Photo photo = photoServices.getById(photoId);
            photoServices.delete(photo, user, contest);
            return "redirect:/contests/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    @GetMapping("/{contestId}/photos/{photoId}/review")
    public String showCreateReview(@PathVariable Long contestId, @PathVariable Long photoId,
                                   Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            photoServices.getPhotoByContestId(photoId, contestId);
            model.addAttribute("review", new PhotoReviewDto());
            return "CreateReviewView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @PostMapping("/{contestId}/photos/{photoId}/review")
    public String handleCreateReview(@PathVariable Long contestId, @PathVariable Long photoId,
                                     Model model, HttpSession session,
                                     @Valid @ModelAttribute("review") PhotoReviewDto photoReviewDto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "CreateReviewView";

        try {
            User user = authenticationHelper.tryGetUser(session);
            Photo photo = photoServices.getPhotoByContestId(photoId, contestId);
            PhotoScore photoScore = modelMapper.dtoToObject(photoReviewDto);
            PhotoReviewDetails photoReviewDetails = modelMapper.dtoToReviewDetails(photoReviewDto);
            photoServices.postReview(photoScore, photo, user, photoReviewDetails);
            return "redirect:/contests/" + contestId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("comment", "review_exists", e.getMessage());
            return "CreateReviewView";
        }
    }
}
