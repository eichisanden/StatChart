import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VmStat {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            return;
        }
        String targetFile = args[0];

        XYChart chartProcs = new XYChartBuilder().title("procs").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("number of process").build();
        XYChart chartMemory = new XYChartBuilder().title("memory").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("Mbytes").build();
        XYChart chartSwap = new XYChartBuilder().title("swap").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("KB/s").build();
        XYChart chartIo = new XYChartBuilder().title("io").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("blocks/s").build();
        XYChart chartSystem = new XYChartBuilder().title("system").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("number of times/s").build();
        XYChart chartCpu = new XYChartBuilder().title("cpu").theme(Styler.ChartTheme.Matlab).xAxisTitle("time").yAxisTitle("percentage").build();
        chartCpu.getStyler().setYAxisMax(100d);
        chartCpu.getStyler().setYAxisMin(0d);
        chartCpu.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        List<Integer> time = new ArrayList<>();
        List<Integer> r = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        List<Long> swapd = new ArrayList<>();
        List<Long> free = new ArrayList<>();
        List<Long> buff = new ArrayList<>();
        List<Long> cache = new ArrayList<>();
        List<Integer> si = new ArrayList<>();
        List<Integer> so = new ArrayList<>();
        List<Integer> bi = new ArrayList<>();
        List<Integer> bo = new ArrayList<>();
        List<Integer> in = new ArrayList<>();
        List<Integer> cs = new ArrayList<>();
        List<Integer> usr = new ArrayList<>();
        List<Integer> sys = new ArrayList<>();
        List<Integer> wait = new ArrayList<>();
        List<Integer> st = new ArrayList<>();
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(targetFile)))) {

            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();

                if (line == null || line.contains("--") || line.contains("swpd")) {
                    continue;
                }

                String[] items = line.trim().split("\\s+");
                r.add(Integer.parseInt(items[0]));
                b.add(Integer.parseInt(items[1]));

                long lSwapd = Long.parseLong(items[2]);
                long lFree = Long.parseLong(items[3]);
                long lBuff = Long.parseLong(items[4]);
                long lCache = Long.parseLong(items[5]);
                swapd.add(lSwapd / 1024);
                free.add(lFree / 1024);
                buff.add(lBuff / 1024);
                cache.add(lCache / 1024);

                si.add(Integer.parseInt(items[6]));
                so.add(Integer.parseInt(items[7]));
                bi.add(Integer.parseInt(items[8]));
                bo.add(Integer.parseInt(items[9]));
                in.add(Integer.parseInt(items[10]));
                cs.add(Integer.parseInt(items[11]));

                int iUsr = Integer.parseInt(items[12]);
                int iSys = Integer.parseInt(items[13]);
                int iWait = Integer.parseInt(items[15]);
                int iSt = Integer.parseInt(items[16]);
                usr.add(iUsr);
                sys.add(iUsr + iSys);
                wait.add(iUsr + iSys + iWait);
                st.add(iUsr + iSys + iWait + iSt);

                time.add(index++);
            }
        }

        chartProcs.addSeries("r", time, r).setMarker(SeriesMarkers.NONE);
        chartProcs.addSeries("b", time, b).setMarker(SeriesMarkers.NONE);
        chartMemory.addSeries("swpd", time, swapd).setMarker(SeriesMarkers.NONE);
        chartMemory.addSeries("free", time, free).setMarker(SeriesMarkers.NONE);
        chartMemory.addSeries("buff", time, buff).setMarker(SeriesMarkers.NONE);
        chartMemory.addSeries("cache", time, cache).setMarker(SeriesMarkers.NONE);
        chartSwap.addSeries("si", time, si).setMarker(SeriesMarkers.NONE);
        chartSwap.addSeries("so", time, so).setMarker(SeriesMarkers.NONE);
        chartIo.addSeries("bi", time, bi).setMarker(SeriesMarkers.NONE);
        chartIo.addSeries("bo", time, bo).setMarker(SeriesMarkers.NONE);
        chartSystem.addSeries("in", time, in).setMarker(SeriesMarkers.NONE);
        chartSystem.addSeries("cs", time, cs).setMarker(SeriesMarkers.NONE);
        chartCpu.addSeries("st", time, st).setMarker(SeriesMarkers.NONE);
        chartCpu.addSeries("wait", time, wait).setMarker(SeriesMarkers.NONE);
        chartCpu.addSeries("sys", time, sys).setMarker(SeriesMarkers.NONE);
        chartCpu.addSeries("usr", time, usr).setMarker(SeriesMarkers.NONE);

        new SwingWrapper<>(Arrays.asList(chartProcs, chartMemory, chartSwap, chartIo, chartSystem, chartCpu)).displayChartMatrix();
    }
}
