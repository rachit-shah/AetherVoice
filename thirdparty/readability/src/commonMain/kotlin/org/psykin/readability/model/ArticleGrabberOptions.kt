package org.psykin.readability.model

open class ArticleGrabberOptions(
  var stripUnlikelyCandidates: Boolean = true,
  var weightClasses: Boolean = true,
  var cleanConditionally: Boolean = true
)
