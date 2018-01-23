package com.meiyou.atom.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import com.meiyou.atom.AtomClassVisitor
import com.meiyou.atom.AtomClazzMaker
import com.meiyou.atom.AtomClazzNode
import com.meiyou.atom.AtomMakeClassVisitor
import com.meiyou.atom.AtomMetaWriter
import com.meiyou.atom.AtomNode
import com.meiyou.atom.AtomTodoNode
import com.meiyou.atom.AtomVar
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

/**
 * Created by Linhh on 17/5/31.
 */

public class PluginImpl extends Transform implements Plugin<Project> {
    private List<AtomNode> mAtomNodeList = new ArrayList<>();
    private int mIndex = -1;


    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension);
        android.registerTransform(this)
    }


    @Override
    public String getName() {
        return "Atom";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT;
        def name = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.name()
        def deprecated = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.getClass()
                .getField(name).getAnnotation(Deprecated.class)

        if (deprecated == null) {
//            println "cannot find QualifiedContent.Scope.PROJECT_LOCAL_DEPS Deprecated.class "
            return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT
                    , QualifiedContent.Scope.PROJECT_LOCAL_DEPS
                    , QualifiedContent.Scope.EXTERNAL_LIBRARIES
                    , QualifiedContent.Scope.SUB_PROJECTS
                    , QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS)
        } else {
//            println "find QualifiedContent.Scope.PROJECT_LOCAL_DEPS Deprecated.class "
            return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT
                    , QualifiedContent.Scope.EXTERNAL_LIBRARIES
                    , QualifiedContent.Scope.SUB_PROJECTS)
        }
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
    }


    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println '==================ATOM transform start=================='
        //遍历inputs里的TransformInput
        if(outputProvider!=null)
            outputProvider.deleteAll()
        inputs.each { TransformInput input ->
            //遍历input里边的DirectoryInput
            input.directoryInputs.each {
                DirectoryInput directoryInput ->
                    //是否是目录
                    if (directoryInput.file.isDirectory()) {
                        //遍历目录
                        directoryInput.file.eachFileRecurse {
                            File file ->
                                def filename = file.name;
                                def name = file.name
                                //这里进行我们的处理 TODO
                                if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                        !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {
                                    ClassReader classReader = new ClassReader(file.bytes)
                                    ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                                    AtomClassVisitor cv = new AtomClassVisitor(Opcodes.ASM5,classWriter, mIndex)
                                    classReader.accept(cv, EXPAND_FRAMES)
                                    if(cv.mTodoNode != null){
                                        for(AnnotationNode node: cv.mTodoNode){
                                            if(node != null && node.values != null){
                                                AtomTodoNode todonode = AtomClazzMaker.makeTodoClazzNode(AtomVar.TYPE_TODO, node.values)
                                                println "*************************************************"
                                                println "注：Atom 找到一个 TODO , 你应该把它修复， 位置： " + file.getAbsolutePath()
                                                println "注：编写 TODO 的人给你留下了一个提示-->" + todonode.msg
                                                if(!todonode.expried) {
                                                    println "注：这是一个可延迟处理的TODO，你可以后续再来处理，不会影响到编译"
                                                }
                                                println "*************************************************"

                                                if(todonode.expried) {
                                                    throw new Exception("注：这个TODO要求你必须修复，当你修复后，编译才会继续")
                                                }
                                            }
                                        }
                                    }

                                    if(cv.mAtomNodes != null){
                                        mIndex = cv.mIndex
                                        mAtomNodeList.addAll(cv.mAtomNodes)
                                        for(AtomNode atomNode : cv.mAtomNodes){
                                            println "Atom find:" + atomNode
                                        }
                                    }
                                    //类注解处理
                                    int type = cv.mClazzType
                                    AtomClazzNode clazzNode = null;
                                    if(type != -1){
                                        //存在类处理
                                        if(cv.mClazzNode != null && cv.mClazzNode.values != null){
                                            clazzNode = AtomClazzMaker.makeClazzNode(type, cv.mClazzNode.values)
                                        }
                                    }
                                    println name
                                    ClassWriter makeClassWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                                    AtomMakeClassVisitor makecv = new AtomMakeClassVisitor(Opcodes.ASM5,makeClassWriter, cv.mClazzName, cv.mAtomNodes,clazzNode,cv.mForeachNode )
                                    classReader.accept(makecv, EXPAND_FRAMES)
                                    byte[] code = makeClassWriter.toByteArray()
                                    FileOutputStream fos = new FileOutputStream(
                                            file.parentFile.absolutePath + File.separator + name)
                                    fos.write(code)
                                    fos.close()

//                                    println 'Anna-----> inject file:' + file.getAbsolutePath()
                                }
//                                println 'Assassin-----> find file:' + file.getAbsolutePath()
                                //project.logger.
                        }
                    }
                    //处理完输入文件之后，要把输出给下一个任务
                    def dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.copyDirectory(directoryInput.file, dest)
            }


            input.jarInputs.each { JarInput jarInput ->
                /**
                 * 重名名输出文件,因为可能同名,会覆盖
                 */
                def jarName = jarInput.name
//                println "Anna jarName:" + jarName + "; "+ jarInput.file.absolutePath
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {

                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                File tmpFile = null;
                if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
                    JarFile jarFile = new JarFile(jarInput.file);
                    Enumeration enumeration = jarFile.entries();
                    tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_anna.jar");
                    //避免上次的缓存被重复插入
                    if(tmpFile.exists()) {
                        tmpFile.delete();
                    }
                    JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile));
                    //用于保存
                    ArrayList<String> processorList = new ArrayList<>();
                    while (enumeration.hasMoreElements()) {
                        JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                        String entryName = jarEntry.getName();
                        ZipEntry zipEntry = new ZipEntry(entryName);

                        InputStream inputStream = jarFile.getInputStream(jarEntry);
                        //如果是inject文件就跳过
                        println entryName
                        if (entryName.endsWith(".class") && !entryName.contains("R\$") &&
                                !entryName.contains("R.class") && !entryName.contains("BuildConfig.class")) {
                            //class文件处理
//                            println "entryName anna:" + entryName
                            jarOutputStream.putNextEntry(zipEntry);
                            ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                            ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                            AtomClassVisitor cv = new AtomClassVisitor(Opcodes.ASM5,classWriter, mIndex)
                            classReader.accept(cv, EXPAND_FRAMES)
                            if(cv.mAtomNodes != null){
                                mIndex = cv.mIndex
                                mAtomNodeList.addAll(cv.mAtomNodes)
                                for(AtomNode atomNode : cv.mAtomNodes){
                                    println "Atom find:" + atomNode
                                }
                            }
                            //类注解处理
                            int type = cv.mClazzType
                            AtomClazzNode clazzNode = null;
                            if(type != -1){
                                //存在类处理
                                if(cv.mClazzNode != null && cv.mClazzNode.values != null){
                                    clazzNode = AtomClazzMaker.makeClazzNode(type, cv.mClazzNode.values)
                                }
                            }
                            ClassWriter makeClassWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                            AtomMakeClassVisitor makecv = new AtomMakeClassVisitor(Opcodes.ASM5,makeClassWriter, cv.mClazzName, cv.mAtomNodes,clazzNode ,cv.mForeachNode)
                            classReader.accept(makecv, EXPAND_FRAMES)
                            byte[] code = makeClassWriter.toByteArray()
                            jarOutputStream.write(code);
                        } else if(entryName.contains("META-INF/services/javax.annotation.processing.Processor")){
                            if(!processorList.contains(entryName)){
//                                println "entryName no anna:" + entryName
                                processorList.add(entryName)
                                jarOutputStream.putNextEntry(zipEntry);
                                jarOutputStream.write(IOUtils.toByteArray(inputStream));
                            }else{
//                                println "duplicate entry:" + entryName
                            }
                        }else {
//                            println "entryName no anna:" + entryName
                            jarOutputStream.putNextEntry(zipEntry);
                            jarOutputStream.write(IOUtils.toByteArray(inputStream));
                        }
                        jarOutputStream.closeEntry();
                    }
                    //结束
                    jarOutputStream.close();
                    jarFile.close();
//                    jarInput.file.delete();
//                    tmpFile.renameTo(jarInput.file);
                }
//                println 'Assassin-----> find Jar:' + jarInput.getFile().getAbsolutePath()

                //处理jar进行字节码注入处理 TODO

                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
//                println 'Anna-----> copy to Jar:' + dest.absolutePath
                if(tmpFile == null) {
                    FileUtils.copyFile(jarInput.file, dest)
                }else{
                    FileUtils.copyFile(tmpFile, dest)
                    tmpFile.delete()
                }
            }
        }

        //创建meta数据
        File meta_file = outputProvider.getContentLocation("atom_inject_metas", getOutputTypes(), getScopes(),
                Format.JAR);
        if(!meta_file.getParentFile().exists()){
            meta_file.getParentFile().mkdirs();
        }
        if(meta_file.exists()){
            meta_file.delete();
        }

        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(meta_file));

        for(int i = 0; i < mAtomNodeList.size(); i ++){
            ZipEntry addEntry = new ZipEntry("com/meiyou/atom/metas/AtomMeta" + i +".class");
            jarOutputStream.putNextEntry(addEntry);
            jarOutputStream.write(AtomMetaWriter.makeMetas(i,mAtomNodeList.get(i)));
            jarOutputStream.closeEntry();
        }
        //结束
        jarOutputStream.close();

        println '==================ATOM transform end=================='
    }
}