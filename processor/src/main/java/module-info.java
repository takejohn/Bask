module jp.takejohn.bask.processor {

    requires java.compiler;
    requires org.jetbrains.annotations;
    provides javax.annotation.processing.Processor with jp.takejohn.bask.processor.SkriptSyntaxProcessor;

}
