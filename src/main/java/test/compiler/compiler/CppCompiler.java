package test.compiler.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
@Slf4j
public class CppCompiler {
    public String runCode(String code, String input) {
        log.info("input : {}", input);
        StringBuilder output = new StringBuilder();
        try {
            // 임시 파일.cpp 생성
            File cppSource = File.createTempFile("temp_source", ".cpp");
            File exeFile = File.createTempFile("temp_executable", "");

            try (FileWriter writer = new FileWriter(cppSource)) {
                writer.write(code);
            }

            // C++ 컴파일 명령어 생성
            String gppExecutable = "/usr/bin/g++";
            ProcessBuilder compilePb = new ProcessBuilder(gppExecutable, "-o", exeFile.getAbsolutePath(), cppSource.getAbsolutePath());
            Process compileProcess = compilePb.start();

            // 컴파일 에러 읽기
            BufferedReader compileErrorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            String line;
            while ((line = compileErrorReader.readLine()) != null) {
                output.append("COMPILER ERROR: ").append(line).append("\n");
            }

            // 컴파일 프로세스 종료 상태 확인
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                output.append("Compilation failed with code: ").append(compileExitCode).append("\n");
                cppSource.delete();
                exeFile.delete();
                return output.toString();
            }

            // C++ 실행 명령어 생성
            ProcessBuilder runPb = new ProcessBuilder(exeFile.getAbsolutePath());
            Process runProcess = runPb.start();

            // 사용자 입력 제공
            try (BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                String[] inputs = input.split("\\s+");
                for (String inputLine : inputs) {
                    processInput.write(inputLine);
                    processInput.newLine();
                }
                processInput.flush();
                processInput.close(); // 입력 스트림을 닫음
            }

            // 실행 출력 읽기
            BufferedReader runReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            while ((line = runReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 실행 에러 읽기
            BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            while ((line = runErrorReader.readLine()) != null) {
                output.append("RUNTIME ERROR: ").append(line).append("\n");
            }

            // 실행 프로세스 종료 상태 확인
            int runExitCode = runProcess.waitFor();
            output.append("Exited with code: ").append(runExitCode);

            // 임시 파일 삭제
            cppSource.delete();
            exeFile.delete();

        } catch (Exception e) {
            output.append("Exception: ").append(e.getMessage());
        }
        return output.toString();
    }
}

