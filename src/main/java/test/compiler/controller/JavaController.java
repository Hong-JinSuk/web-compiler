//package test.compiler.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import test.compiler.compiler.JavaCompiler;
//
//@Controller
//public class JavaController {
//
//    @Autowired
//    JavaCompiler javaCompiler;
//
//    @PostMapping("/runCode")
//    public String runJavaScript(
//            @RequestParam("code") String code,
//            Model model
//    ) {
//        String result = javaCompiler.runCode(code);
//        model.addAttribute("result", result);
//        return "result";
//    }
//}
