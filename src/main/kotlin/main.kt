import jetbrains.datalore.plot.PlotHtmlExport
import jetbrains.datalore.plot.PlotHtmlHelper
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomHistogram
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.intern.toSpec
import jetbrains.letsPlot.letsPlot
import java.awt.Desktop
import java.io.File

fun main() {
    val inputFileName = "src/main/resources/hash.txt"
    val input = readFileAsLinesUsingBufferedReader(inputFileName).map { it.toInt() }.toIntArray()
    val result = hash(input)
    println(result.contentToString())

    //plot records
    val p = plot(result)

    // Export to HTML.
    // Note: if all you need is to save HTML to a file than you can just use the 'ggsave()' function.
    val content = PlotHtmlExport.buildHtmlFromRawSpecs(p.toSpec(), PlotHtmlHelper.scriptUrl("2.0.4"))
    ggsave(p, "myPlot.svg")
    openInBrowser(content)

}

/**
 * Method to hash the input array
 *
 * @param input intArray
 * @return Hashed array
 */
fun hash(input: IntArray): IntArray {
    return input.map { it.mod(10007) }.toIntArray()
    //Since input is an intArray, where the value is the key at the same time
    //we can return same array as hash function
    //return input

}

/**
 * Plot Result Array
 *
 * @param result Result Array to Plot
 * @return Plot
 */
fun plot(result: IntArray): Plot {
    val data = mapOf(
        "hash" to List(result.size) { "A" },
        "index" to result
    )

    var p = letsPlot(data) { x = "index" }
    p += geomHistogram(binWidth = 100) + ggsize(1500, 700)
    return p
}

/**
 * Method to open plot in browser
 *
 * @param content Content to open in browser and save in my_plot.html file
 */
fun openInBrowser(content: String) {
    val dir = File(System.getProperty("user.dir"), "lets-plot-images")
    dir.mkdir()
    val file = File(dir.canonicalPath, "my_plot.html")
    file.createNewFile()
    file.writeText(content)

    Desktop.getDesktop().browse(file.toURI())
}

/**
 * Read file
 *
 * @param fileName Read file
 * @return List of strings read
 */
fun readFileAsLinesUsingBufferedReader(fileName: String): List<String> = File(fileName).bufferedReader().readLines()