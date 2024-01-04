module Bask.annotations.main {

    requires java.compiler;
    requires org.jetbrains.annotations;
    exports jp.takejohn.bask.annotations;
    provides javax.annotation.processing.Processor with jp.takejohn.bask.annotations.SkriptSyntaxProcessor;

}
