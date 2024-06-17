package test.compiler.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class PythonCompiler {
    public String runCode(String code, String input) {
        log.info("input : {}", input);
        StringBuilder output = new StringBuilder();
        try {
            // 임시 파일.py 생성
            File pythonScript = File.createTempFile("temp_script", ".py");
            try (FileWriter writer = new FileWriter(pythonScript)) {
                writer.write(code);
            }

            // 파이썬 실행 명령어 생성
            String pythonExecutable = "/usr/bin/python3";
            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, pythonScript.getAbsolutePath());
            Process process = pb.start();

            // 사용자 입력 제공
            try (BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                String[] inputs = input.split("\\s+");
                for (String inputLine : inputs) {
                    processInput.write(inputLine);
                    processInput.newLine();
                }
                processInput.flush();
                processInput.close(); // 입력 스트림을 닫음
            }
            String line;
            // 파이썬 스크립트의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 파이썬 스크립트의 에러 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            // 프로세스 종료 상태 확인
            int exitCode = process.waitFor();
            output.append("Exited with code: ").append(exitCode);

            // 임시 파일 삭제
            pythonScript.delete();

        } catch (Exception e) {
            output.append("Exception: ").append(e.getMessage());
        }
        return output.toString();
    }
}

