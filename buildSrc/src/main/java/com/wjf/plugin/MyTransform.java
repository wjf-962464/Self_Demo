package com.wjf.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** @author wangjiafeng */
public class MyTransform extends Transform {
    @Override
    public String getName() {
        return "wjf";
    }

    /**
     * 类型
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return null;
    }

    /**
     * 作用域
     *
     * @return
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return null;
    }

    /**
     * 增量
     *
     * @return
     */
    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        inputs.forEach(
                transformInput -> {
                    Collection<JarInput> jarInputs = transformInput.getJarInputs();
                    Collection<DirectoryInput> directoryInputs =
                            transformInput.getDirectoryInputs();
                    directoryInputs.forEach(
                            directoryInput -> {
                                Map<File, Status> changedFiles = directoryInput.getChangedFiles();
                                changedFiles.forEach(
                                        (key, value) -> {
                                            System.out.println(key + "===>" + value);
                                        });
                            });
                });
        super.transform(transformInvocation);
    }
}
