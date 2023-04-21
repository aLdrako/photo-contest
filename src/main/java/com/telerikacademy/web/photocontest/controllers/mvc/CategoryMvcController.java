package com.telerikacademy.web.photocontest.controllers.mvc;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.EntityDuplicateException;
import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Category;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dto.CategoryDto;
import com.telerikacademy.web.photocontest.services.ModelMapper;
import com.telerikacademy.web.photocontest.services.contracts.CategoryServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryMvcController extends BaseMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final CategoryServices categoryServices;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showAllCategories(Model model, HttpSession session)  {
        try {
            authenticationHelper.tryGetOrganizer(session);
            Iterable<Category> categories = categoryServices.findAll();
            model.addAttribute("categories", categories);
            return "CategoriesView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/create")
    public String showCategoryCreate(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            model.addAttribute("category", new CategoryDto());
            return "CategoryCreateView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto,
                                 BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) return "CategoryCreateView";

        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            Category category = modelMapper.dtoToObject(categoryDto);
            categoryServices.save(category, user);
            return "redirect:/categories";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (EntityDuplicateException e)  {
            bindingResult.rejectValue("name", "duplicate_name", e.getMessage());
            return "CategoryCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showCategoryUpdate(@PathVariable Long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetOrganizer(session);
            Category category = categoryServices.findById(id);
            model.addAttribute("category", category);
            return "CategoryUpdateView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("category") CategoryDto categoryDto,
                                 BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) return "CategoryUpdateView";

        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            Category category = modelMapper.dtoToObject(id, categoryDto);
            categoryServices.save(category, user);
            return "redirect:/categories";
        }  catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (EntityDuplicateException e)  {
            bindingResult.rejectValue("name", "duplicate_name", e.getMessage());
            return "CategoryUpdateView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetOrganizer(session);
            categoryServices.deleteById(id, user);
            return "redirect:/categories";
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
}
