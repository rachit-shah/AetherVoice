package org.psykin.readability.extended.util

import org.psykin.readability.util.RegExUtil

open class RegExUtilExtended : RegExUtil {

  companion object {
    const val REMOVE_IMAGE_DEFAULT_PATTERN = "author|avatar|thumbnail" // CHANGE: this is not in Mozilla's Readability

    const val NEGATIVE_DEFAULT_PATTERN_EXTENDED = "|float"
  }

  protected val removeImage: Regex

  constructor(unlikelyCandidatesPattern: String = UNLIKELY_CANDIDATES_DEFAULT_PATTERN, okMaybeItsACandidatePattern: String = OK_MAYBE_ITS_A_CANDIDATE_DEFAULT_PATTERN,
              positivePattern: String = POSITIVE_DEFAULT_PATTERN, negativePattern: String = NEGATIVE_DEFAULT_PATTERN + NEGATIVE_DEFAULT_PATTERN_EXTENDED,
              extraneousPattern: String = EXTRANEOUS_DEFAULT_PATTERN, bylinePattern: String = BYLINE_DEFAULT_PATTERN,
              replaceFontsPattern: String = REPLACE_FONTS_DEFAULT_PATTERN, normalizePattern: String = NORMALIZE_DEFAULT_PATTERN,
              videosPattern: String = VIDEOS_DEFAULT_PATTERN, nextLinkPattern: String = NEXT_LINK_DEFAULT_PATTERN,
              prevLinkPattern: String = PREV_LINK_DEFAULT_PATTERN, whitespacePattern: String = WHITESPACE_DEFAULT_PATTERN,
              hasContentPattern: String = HAS_CONTENT_DEFAULT_PATTERN, removeImagePattern: String = REMOVE_IMAGE_DEFAULT_PATTERN)
          : super(unlikelyCandidatesPattern, okMaybeItsACandidatePattern, positivePattern, negativePattern, extraneousPattern, bylinePattern, replaceFontsPattern, normalizePattern,
    videosPattern, nextLinkPattern, prevLinkPattern, whitespacePattern, hasContentPattern) {
    this.removeImage = Regex(removeImagePattern)
  }


  open fun keepImage(matchString: String): Boolean { // CHANGE: this is not in Mozilla's Readability
    if((isNegative(matchString) && isPositive(matchString) == false) || removeImage.containsMatchIn(matchString)) {
      return false
    }
    return true
  }
}
