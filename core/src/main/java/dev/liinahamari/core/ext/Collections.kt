package dev.liinahamari.core.ext

fun <E> List<E>.getSelectedIndices(selected: List<E>): IntArray = mapIndexed { index, value -> index to value }.filter { it.second in selected }.map { it.first }.toIntArray()
fun <E> Array<E>.getSelectedIndices(selected: List<E>): IntArray = toList().getSelectedIndices(selected)
