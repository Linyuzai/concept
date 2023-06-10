package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.impl.PsiNameHelperImpl
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

fun interface ConceptTextValidationFunction {
    fun checkText(fieldText: String): String?
}

val CHECK_NOT_EMPTY = ConceptTextValidationFunction { fieldText: String ->
    if (fieldText.isEmpty()) {
        return@ConceptTextValidationFunction "Field must be set"
    }
    null
}

val CHECK_NO_WHITESPACES = ConceptTextValidationFunction { fieldText: String ->
    if (fieldText.contains(" ")) {
        return@ConceptTextValidationFunction "Space symbols are not allowed here"
    }
    null
}

val CHECK_SIMPLE_NAME_FORMAT: ConceptTextValidationFunction = object : ConceptTextValidationFunction {
    private val myPattern = Pattern.compile("[a-zA-Z0-9-._ ]*") // IDEA-235441
    override fun checkText(fieldText: String): String? {
        return if (!myPattern.matcher(fieldText).matches()) {
            "Only Latin characters, digits, spaces, '-', '_' and '.' are allowed here"
        } else null
    }
}

val CHECK_ARTIFACT_SIMPLE_FORMAT: ConceptTextValidationFunction = object : ConceptTextValidationFunction {
    private val myUsedSymbolsCheckPattern = Pattern.compile("[a-zA-Z0-9-_]*")
    private val myFirstSymbolCheckPattern = Pattern.compile("[a-zA-Z_].*")
    override fun checkText(fieldText: String): String? {
        if (!myUsedSymbolsCheckPattern.matcher(fieldText).matches()) {
            return "Only Latin characters, digits, '-' and '_' are allowed here"
        }
        return if (!myFirstSymbolCheckPattern.matcher(fieldText).matches()) {
            "Must start with Latin character or '_'"
        } else null
    }
}

// IDEA-235887 prohibit using some words reserved by Windows in group and artifact fields
val CHECK_NO_RESERVED_WORDS: ConceptTextValidationFunction = object : ConceptTextValidationFunction {
    private val myPattern = Pattern.compile(
        "(^|[ .])(con|prn|aux|nul|com\\d|lpt\\d)($|[ .])",
        Pattern.CASE_INSENSITIVE
    )

    override fun checkText(fieldText: String): String? {
        return if (myPattern.matcher(fieldText).find()) {
            "Parts 'con', 'prn', 'aux', 'nul', 'com0', ..., 'com9' and 'lpt0', ..., 'lpt9' are not allowed here"
        } else null
    }
}

val CHECK_PACKAGE_NAME = ConceptTextValidationFunction { fieldText: String? ->
    if (!PsiNameHelperImpl.getInstance().isQualifiedName(fieldText)) {
        return@ConceptTextValidationFunction "'$fieldText' is not a valid package name"
    }
    null
}

val CHECK_GROUP_FORMAT: ConceptTextValidationFunction = object : ConceptTextValidationFunction {
    private val myPatternForEntireText = Pattern.compile("[a-zA-Z\\d_.-]*")
    private val myPatternForOneWord = Pattern.compile("[a-zA-Z_].*")
    override fun checkText(fieldText: String): String? {
        if (!myPatternForEntireText.matcher(fieldText).matches()) {
            return "Only Latin characters, digits, '_', '-' and '.' are allowed here"
        }
        val firstSymbol = fieldText[0]
        val lastSymbol = fieldText[fieldText.length - 1]
        if (firstSymbol == '.' || lastSymbol == '.') {
            return "Must not start or end with '.'"
        }
        if (fieldText.contains("..")) {
            return "Must not contain '..' sequences"
        }
        val wordsBetweenDots = fieldText.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (word in wordsBetweenDots) {
            if (!myPatternForOneWord.matcher(word).matches()) {
                return "Part '$word' is incorrect, it must start with Latin character or ''_''"
            }
        }
        return null
    }
}

val CHECK_LOCATION_FOR_ERROR = ConceptTextValidationFunction { fieldText: String? ->
    val locationPath: Path
    locationPath = try {
        Paths.get(FileUtil.expandUserHome(fieldText!!))
    } catch (e: InvalidPathException) {
        return@ConceptTextValidationFunction "The specified path is incorrect"
    }
    val file = locationPath.toFile()
    if (file.exists()) {
        if (!file.canWrite()) {
            return@ConceptTextValidationFunction "Directory is not writable"
        }
        val children = file.list()
            ?: return@ConceptTextValidationFunction "The specified path is not a directory"
    }
    null
}

val CHECK_LOCATION_FOR_WARNING = ConceptTextValidationFunction { fieldText: String? ->
    try {
        val file =
            Paths.get(FileUtil.expandUserHome(fieldText!!)).toFile()
        if (file.exists()) {
            val children = file.list()
            if (children != null && children.size > 0) {
                return@ConceptTextValidationFunction "Directory is not empty"
            }
        }
    } catch (ipe: InvalidPathException) {
        return@ConceptTextValidationFunction null
    }
    null
}