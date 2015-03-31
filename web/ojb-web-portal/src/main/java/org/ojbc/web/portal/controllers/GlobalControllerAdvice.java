package org.ojbc.web.portal.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Value("${bannerPath:/static/images/banner/Banner.png}")
    String bannerPath;

    @Value("${themePath:/static/css/style.css}")
    String themePath;

    @ModelAttribute
    public void setupModelAttributes(Model model) {
        model.addAttribute("bannerPath", bannerPath);
        model.addAttribute("themePath", themePath);
    }
}