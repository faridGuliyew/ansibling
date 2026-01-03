package dev.faridg.ansibling.presentation.execute_ci

fun String.renderJinja(
    variables: Map<String, String>
): String {
    var result = this

    // 1. Handle {% if var %} ... {% endif %}
    val ifRegex = Regex(
        """\{%\s*if\s+([a-zA-Z0-9_]+)\s*%\}(.*?)\{%\s*endif\s*%\}""",
        setOf(RegexOption.DOT_MATCHES_ALL)
    )

    result = ifRegex.replace(result) { match ->
        val key = match.groupValues[1]
        val content = match.groupValues[2]

        val rawValue = variables[key]?.lowercase() ?: error("Jinja error: variable '$key' does not exist")
        val condition = when (rawValue) {
            "true", "1", "yes", "on" -> true
            else -> false
        }

        if (condition) content else ""
    }

    // 2. Handle {{ var }}
    val varRegex = Regex("""\{\{\s*([a-zA-Z0-9_]+)\s*\}\}""")
    result = varRegex.replace(result) { match ->
        val key = match.groupValues[1]
        variables[key] ?: error("Jinja error: variable '$key' does not exist")
    }

    return result
}
