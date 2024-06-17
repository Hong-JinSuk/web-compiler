package test.compiler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.compiler.compiler.CppCompiler;
import test.compiler.compiler.JavaCompiler;
import test.compiler.compiler.JavascriptCompiler;
import test.compiler.compiler.PythonCompiler;

@Controller
public class CompilerController {

    @Autowired
    PythonCompiler pythonCompiler;

    @Autowired
    JavaCompiler javaCompiler;

    @Autowired
    CppCompiler cppCompiler;

    @Autowired
    JavascriptCompiler javascriptCompiler;

    @GetMapping
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/runCode")
    public String runCode(
            @RequestParam("language") String language,
            @RequestParam("code") String code,
            @RequestParam("input") String input,
            Model model
    ) {
        String output = langCompile(language, code, input);
        model.addAttribute("language", language);
        model.addAttribute("code", code);
        model.addAttribute("input", input);
        model.addAttribute("result", output);
        return "upload";
    }

    private String langCompile(String language, String code, String input) {
        String result;
        if ("python".equals(language)) {
            result = pythonCompiler.runCode(code, input);
//            pythonCompiler.runTestCases(code);
        } else if ("java".equals(language)) {
            result = javaCompiler.runCode(code, input);
        } else if ("C++".equals(language)) {
            result = cppCompiler.runCode(code, input);
        } else if ("javascript".equals(language)) {
            result = javascriptCompiler.runCode(code, input);
        } else {
            result = "Unsupported language: " + language;
        }
        return result;
    }

}
