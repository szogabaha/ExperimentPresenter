package core

import java.util.SortedMap


data class Inference(val languageResults: SortedMap<String, Double>, val isValid: Boolean = true)