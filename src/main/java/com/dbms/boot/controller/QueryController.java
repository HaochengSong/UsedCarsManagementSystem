package com.dbms.boot.controller;

import com.dbms.boot.domain.Listing;
import com.dbms.boot.entities.*;
import com.dbms.boot.repository.*;
//import com.dbms.boot.object.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;

@Controller
public class QueryController {
    @Autowired
    private QueryRepository queryRepository;

    private Integer bigToInt(BigDecimal b) {
        if (b == null) return null;
        return b.intValue();
    }

    @GetMapping("/search/form")
    public String form0(Model model) {
        SearchPara p = new SearchPara();
        model.addAttribute("para0", p);
        return "search/search-form";
    }
    @GetMapping("/search/show")
    public String show0(Model model, HttpServletRequest request) {
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        String minPriceStr = request.getParameter("minPrice"), maxPriceStr = request.getParameter("maxPrice");
        String manufacturer = request.getParameter("manufacturer"), modelName = request.getParameter("modelName");
        String minOdoStr = request.getParameter("minOdo"), maxOdoStr = request.getParameter("maxOdo");
        String state = request.getParameter("state"), status = request.getParameter("status"), color = request.getParameter("color");
        if (manufacturer.equals("")) manufacturer = "%";
        if (modelName.equals("")) modelName = "%";
        if (state.equals("")) state = "%";
        if (status.equals("")) status = "%";
        if (color.equals("")) color = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
        Integer minPrice = minPriceStr.equals("") ? 0 : Integer.parseInt(minPriceStr), maxPrice = maxPriceStr.equals("") ? 10000000 : Integer.parseInt(maxPriceStr);
        Integer minOdo = minOdoStr.equals("") ? 0 : Integer.parseInt((minOdoStr)), maxOdo = maxOdoStr.equals("") ? 10000000 : Integer.parseInt(maxOdoStr);
        List<Object[]> objects = queryRepository.showSimple(minYear, maxYear, manufacturer, modelName, minPrice, maxPrice, minOdo, maxOdo, state, status, color);
        List<SearchResPara> list = objects.stream().map(
                o -> new SearchResPara((BigDecimal) o[0], bigToInt((BigDecimal) o[1]), (BigDecimal) o[2], (String) o[3], (BigDecimal) o[4], (String) o[5], (String) o[6], (String) o[7], (String) o[8], (String) o[9], (String) o[10])
        ).collect(Collectors.toList());
        model.addAttribute("res", list);
        return "search/search-show";
    }

    @GetMapping("/trend1/form")
    public String form1(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para1", p);
        return "trend/trend1-form";
    }
    @GetMapping("/trend1/show")
    public String show1(Model model, HttpServletRequest request) throws JsonProcessingException {
        String modelName = request.getParameter("modelName"), condition = request.getParameter("condition"), status = request.getParameter("status"), fuel = request.getParameter("fuel");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (modelName.equals("")) modelName = "%";
        if (condition.equals("")) condition = "%";
        if (status.equals("")) status = "%";
        if (fuel.equals("")) fuel = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
        System.out.println(modelName + " " + maxYear + " " + minYear + " " + condition + " " + status + " " + fuel);
//        List<Object[]> objects = queryRepository.showComplex1("accord", 2019, 2010, "excellent", "clean", "gas");
        List<Object[]> objects = queryRepository.showComplex1(modelName, maxYear, minYear, condition, status, fuel);
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[2]), bigToInt((BigDecimal) o[0]), "Average Price", bigToInt((BigDecimal) o[1]), "Number")
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend1-show";
    }

    @GetMapping("/trend2/form")
    public String form2(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para2", p);
        return "trend/trend2-form";
    }
    @GetMapping("/trend2/show")
    public String show2(Model model, HttpServletRequest request) throws JsonProcessingException {
        String modelName = request.getParameter("modelName"), condition = request.getParameter("condition"), status = request.getParameter("status"), fuel = request.getParameter("fuel");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (modelName.equals("")) modelName = "%";
        if (condition.equals("")) condition = "%";
        if (status.equals("")) status = "%";
        if (fuel.equals("")) fuel = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
        System.out.println(modelName + " " + maxYear + " " + minYear + " " + condition + " " + status + " " + fuel);
//        List<Object[]> objects = queryRepository.showComplex2("tacoma", 2019, 2010, "excellent", "clean", "gas");
        List<Object[]> objects = queryRepository.showComplex2(modelName, maxYear, minYear, condition, status, fuel);
        System.out.println(objects.size());
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[1]), bigToInt((BigDecimal) o[0]), "Average Odometer")
        ).collect(Collectors.toList());
        List<Trend7> listNotNull = new ArrayList<>();
        for (Trend7 t : list) {
            if (t == null || t.getData1() == null || t.getLabel1() == null) continue;
            listNotNull.add(t);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(listNotNull);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend2-show";
    }

    @GetMapping("/trend3/form")
    public String form3(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para3", p);
        return "trend/trend3-form";
    }
    @GetMapping("/trend3/show")
    public String show3(Model model, HttpServletRequest request) throws JsonProcessingException {
        String manufacturer = request.getParameter("manufacturer"), modelName = request.getParameter("modelName"), condition = request.getParameter("condition"), status = request.getParameter("status");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (modelName.equals("")) modelName = "%";
        if (condition.equals("")) condition = "%";
        if (status.equals("")) status = "%";
        if (manufacturer.equals("")) manufacturer = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
//        List<Object[]> objects = queryRepository.showComplex3("%", "accord", "excellent", "%", 2019, 1350);
        List<Object[]> objects = queryRepository.showComplex3(manufacturer, modelName, condition, status, maxYear, minYear);
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[2]), bigToInt((BigDecimal) o[1]), (String) o[0])
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend3-show";
    }

    @GetMapping("/trend4/form")
    public String form4(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para4", p);
        return "trend/trend4-form";
    }
    @GetMapping("trend4/show")
    public String show4(Model model, HttpServletRequest request) throws JsonProcessingException {
        String manufacturer = request.getParameter("manufacturer"), modelName = request.getParameter("modelName"), condition = request.getParameter("condition"), status = request.getParameter("status");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (modelName.equals("")) modelName = "%";
        if (condition.equals("")) condition = "%";
        if (status.equals("")) status = "%";
        if (manufacturer.equals("")) manufacturer = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
//        List<Object[]> objects = queryRepository.showComplex4("%", "golf", "%", "%", 2019, 1350);
        List<Object[]> objects = queryRepository.showComplex4(manufacturer, modelName, condition, status, maxYear, minYear);
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[1]), bigToInt((BigDecimal) o[0]), "Status")
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend4-show";
    }

    @GetMapping("/trend5/form")
    public String form5(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para5", p);
        return "trend/trend5-form";
    }
    @GetMapping("trend5/show")
    public String show5(Model model, HttpServletRequest request) throws JsonProcessingException {
        String manufacturer = request.getParameter("manufacturer"), modelName = request.getParameter("modelName");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (modelName.equals("")) modelName = "%";
        if (manufacturer.equals("")) manufacturer = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
//        List<Object[]> objects = queryRepository.showComplex5("%", "%", 2019, 1350);
        List<Object[]> objects = queryRepository.showComplex5(manufacturer, modelName, maxYear, minYear);
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[1]), bigToInt((BigDecimal) o[0]), "Number of Used Car")
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend5-show";
    }

    @GetMapping("trend6/form")
    public String form6(Model model) {
        QueryPara p = new QueryPara();
        model.addAttribute("para6", p);
        return "trend/trend6-form";
    }
    @GetMapping("trend6/show")
    public String show6(Model model, HttpServletRequest request) throws JsonProcessingException {
        String state = request.getParameter("state"), region = request.getParameter("region");
        String minYearStr = request.getParameter("minYear"), maxYearStr = request.getParameter("maxYear");
        if (state.equals("")) state = "%";
        if (region.equals("")) region = "%";
        Integer minYear = minYearStr.equals("") ? 1350 : Integer.parseInt(minYearStr), maxYear = maxYearStr.equals("") ? 2020 : Integer.parseInt(maxYearStr);
//        List<Object[]> objects = queryRepository.showComplex6("%", "%", 2019, 2010);
        List<Object[]> objects = queryRepository.showComplex6(state, region, maxYear, minYear);
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[1]), bigToInt((BigDecimal) o[0]), "Number of Used Car")
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend6-show";
    }

    @GetMapping("/trend7/form")
    public String form7(Model model){
        return "trend/trend7-form";
    }
    @GetMapping("/trend7/show")
    public String show7(Model model) throws JsonProcessingException {
        List<Object[]> objects = queryRepository.showComplex7();
        List<Trend7> list = objects.stream().map(
                o -> new Trend7(bigToInt((BigDecimal) o[0]), (String) o[1], bigToInt((BigDecimal) o[2]), (String) o[3], bigToInt((BigDecimal) o[4]), (String) o[5], bigToInt((BigDecimal) o[6]))
        ).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
        model.addAttribute("jsonString", jsonString);
        return "trend/trend7-show";
    }
}
