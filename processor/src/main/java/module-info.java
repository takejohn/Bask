module jp.takejohn.bask.processor {

    requires java.compiler;
    provides javax.annotation.processing.Processor with jp.takejohn.bask.processor.SkriptSyntaxProcessor;

}
