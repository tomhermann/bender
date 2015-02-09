package com.zombietank.bender.sigar.influxdb;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.influxdb.dto.Serie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zombietank.bender.influx.series.SeriesBuilder;
import com.zombietank.bender.influx.series.SeriesTransformer;

public class SigarTransformer implements SeriesTransformer<Sigar> {
	private static final Logger logger = LoggerFactory.getLogger(SigarTransformer.class);
	private String hostname;

	@Override
	public Serie[] apply(Sigar sigar) {
		try {
			this.hostname = getHostname();
			return new Serie[] { cpu(sigar.getCpu()), mem(sigar.getMem()), procStat(sigar.getProcStat()),
					swap(sigar.getSwap()) };
		} catch (SigarException e) {
			logger.error("Unable to get system information", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public TimeUnit getPrecision() {
		return TimeUnit.MILLISECONDS;
	}

	private Serie cpu(Cpu cpu) {
		return seriesWithMeta("cpu")
				.columns("idle", "irq", "nice", "softIrq", "stolen", "sys", "total", "user", "wait")
				.values(cpu.getIdle(), cpu.getIrq(), cpu.getNice(), cpu.getSoftIrq(), cpu.getStolen(), cpu.getSys(),
						cpu.getTotal(), cpu.getUser(), cpu.getWait()).build();

	}

	private Serie mem(Mem mem) {
		return seriesWithMeta("mem")
				.columns("actualFree", "actualUsed", "free", "freePercent", "ram", "total", "used", "usedPercent")
				.values(mem.getActualFree(), mem.getActualUsed(), mem.getFree(), mem.getFreePercent(), mem.getRam(),
						mem.getTotal(), mem.getUsed(), mem.getUsedPercent()).build();
	}

	private Serie procStat(ProcStat stat) {
		return seriesWithMeta("procStat")
				.columns("idle", "running", "sleeping", "stopped", "threads", "total", "zombie")
				.values(stat.getIdle(), stat.getRunning(), stat.getSleeping(), stat.getStopped(), stat.getThreads(),
						stat.getTotal(), stat.getZombie()).build();
	}

	private Serie swap(Swap swap) {
		return seriesWithMeta("swap").columns("free", "pageIn", "pageOut", "total", "used")
				.values(swap.getFree(), swap.getPageIn(), swap.getPageOut(), swap.getTotal(), swap.getUsed()).build();
	}

	private SeriesBuilder seriesWithMeta(String name) {
		SeriesBuilder seriesBuilder = new SeriesBuilder(name);
		seriesBuilder.addEntry("hostname", hostname);
		return seriesBuilder;
	}

	private String getHostname() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException ex) {
			return "unknown";
		}
	}
}
