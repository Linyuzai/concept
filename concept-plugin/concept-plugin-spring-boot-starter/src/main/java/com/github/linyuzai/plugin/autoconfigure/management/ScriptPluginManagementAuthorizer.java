package com.github.linyuzai.plugin.autoconfigure.management;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Setter
@Getter
@Deprecated
public class ScriptPluginManagementAuthorizer implements PluginManagementAuthorizer {

    private final ScriptEngine engine;

    private String password;

    public ScriptPluginManagementAuthorizer(String password) throws IOException, ScriptException {
        this.password = password;
        this.engine = createScriptEngine();
    }

    public ScriptPluginManagementAuthorizer() throws IOException, ScriptException {
        this.engine = createScriptEngine();
    }

    protected ScriptEngine createScriptEngine() throws IOException, ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        if (engine == null) {
            throw new IllegalStateException("ScriptEngine is null");
        }

        ClassPathResource resource = new ClassPathResource("concept/plugin/concept-plugin.js");
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                engine.eval(new InputStreamReader(is));
            }
        }

        return engine;
    }

    @Override
    public boolean unlock(String password) {
        if (this.password == null || this.password.isEmpty()) {
            return true;
        }
        try {
            if (engine instanceof Invocable) {
                Object invoked = ((Invocable) engine).invokeFunction("decodePassword", password);
                return this.password.equals(invoked);
            }
        } catch (Throwable ignore) {
        }
        return this.password.equals(password);
    }
}
