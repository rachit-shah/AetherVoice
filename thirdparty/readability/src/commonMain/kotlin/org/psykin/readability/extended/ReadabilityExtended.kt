package org.psykin.readability.extended

import org.psykin.readability.Readability
import org.psykin.readability.extended.processor.ArticleGrabberExtended
import org.psykin.readability.extended.processor.PostprocessorExtended
import org.psykin.readability.extended.util.RegExUtilExtended
import org.psykin.readability.model.ReadabilityOptions
import org.psykin.readability.processor.MetadataParser
import org.psykin.readability.processor.Preprocessor

open class ReadabilityExtended : Readability {
    constructor(uri: String, content: String, options: ReadabilityOptions = ReadabilityOptions(), regExUtil: RegExUtilExtended = RegExUtilExtended(),
                preprocessor: Preprocessor = Preprocessor(regExUtil), metadataParser: MetadataParser = MetadataParser(regExUtil),
                articleGrabber: ArticleGrabberExtended = ArticleGrabberExtended(options, regExUtil), postprocessor: PostprocessorExtended = PostprocessorExtended())
            : super(uri, content, options, regExUtil, preprocessor, metadataParser, articleGrabber, postprocessor)

}
