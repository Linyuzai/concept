package com.github.linyuzai.domain.generator;

import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JavaDomainFileGenerator {

    public static String[] TEMPLATES_BASE = new String[]{
            "Domain.java",
            "DomainImpl.java",
            "Domains.java",
            "DomainFacadeAdapter.java",
            "DomainFacadeAdapterImpl.java",
            "DomainIdGenerator.java",
            "DomainInstantiator.java",
            "DomainInstantiatorImpl.java",
            "DomainController.java",
            "DomainService.java",
            "DomainRepository.java",
            "DomainSearcher.java",
            "DomainSearcherImpl.java"

    };

    public static String[] TEMPLATES_EVENT = new String[]{
            "event/DomainCreatedEvent.java",
            "event/DomainUpdatedEvent.java",
            "event/DomainDeletedEvent.java"
    };

    public static String[] TEMPLATES_VIEW = new String[]{
            "view/DomainCreateCommand.java",
            "view/DomainUpdateCommand.java",
            "view/DomainDeleteCommand.java",
            "view/DomainVO.java",
            "view/DomainQuery.java"
    };

    public static String[] TEMPLATES_SCHRODINGER = new String[]{
            "schrodinger/SchrodingerDomain.java",
            "schrodinger/SchrodingerDomainDomains.java",
            "schrodinger/SchrodingerDomains.java"
    };

    public static String[] TEMPLATES_MBP = new String[]{
            "mbp/DomainPO.java",
            "mbp/DomainMapper.java",
            "mbp/MBPDomainIdGenerator.java",
            "mbp/MBPDomainRepository.java"
    };

    public static String[] TEMPLATES_ALL = Arrays.stream(new String[][]{
                    TEMPLATES_BASE,
                    TEMPLATES_EVENT,
                    TEMPLATES_VIEW,
                    TEMPLATES_SCHRODINGER,
                    TEMPLATES_MBP})
            .flatMap(Arrays::stream)
            .toArray(String[]::new);

    private String _MODULE_;
    private String _PACKAGE_;
    private String _DOMAIN_;
    private String _DESC_;
    private String _UPPER_;
    private String _LOWER_;

    private String[] list;

    public void generate() throws IOException {
        String projectPath = new File("").getAbsolutePath();
        File moduleFile = new File(projectPath, _MODULE_ + "/src/main/java");
        File basePackageFile = new File(moduleFile, _PACKAGE_.replaceAll("\\.", "/"));
        File configPackageFile = new File(basePackageFile, "config");
        File domainPackageFile = new File(basePackageFile, "domain");
        for (String s : list) {
            writeDomain(domainPackageFile, s);
        }
    }

    private void writeDomain(File root, String path) throws IOException {
        int i = path.lastIndexOf("/");
        String dir;
        String name;
        if (i == -1) {
            dir = null;
            name = path;
        } else {
            dir = path.substring(0, i);
            name = path.substring(i + 1);
        }
        File file = new File(root, _DOMAIN_ + (dir == null ? "" : "/" + dir));
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        File java = new File(file, name.replaceAll("Domain", _UPPER_));
        if (!java.exists()) {
            boolean newFile = java.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(java, false))) {
            writer.write(generateContent("domain_templates/java/domain/" + (dir == null ? "" : dir + "/") + name));
        }
    }

    private String generateContent(String name) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        return template(inputStream2String(is));
    }

    private String template(String s) {
        return s.replaceAll("_PACKAGE_", _PACKAGE_)
                .replaceAll("_DOMAIN_", _DOMAIN_)
                .replaceAll("_DESC_", _DESC_)
                .replaceAll("_UPPER_", _UPPER_)
                .replaceAll("_LOWER_", _LOWER_);
    }

    private String inputStream2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String content = reader
                .lines()
                .collect(Collectors.joining("\n"));
        reader.close();
        return content;
    }
}
