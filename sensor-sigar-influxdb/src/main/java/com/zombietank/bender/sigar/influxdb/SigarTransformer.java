package com.zombietank.bender.sigar.influxdb;

import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.influxdb.dto.Serie;

import com.zombietank.bender.influx.SeriesTransformer;

public class SigarTransformer implements SeriesTransformer<Sigar> {

	@Override
	public Serie[] apply(Sigar input) {
		try {
			return new Serie[] { 
				toSeries(input.getCpu()),
				toSeries(input.getMem()),
				toSeries(input.getProcStat()),
				toSeries(input.getSwap())
			};
		} catch (SigarException e) {
			// FIXME: If this fails it should log and continue. Need to set up logging, so this will do.
			throw new RuntimeException(e);
		}
	}

	@Override
	public TimeUnit getPrecision() {
		return TimeUnit.MILLISECONDS;
	}

	private Serie toSeries(Cpu cpu) {
		return new Serie.Builder("cpu")
				.columns("idle", "irq", "nice", "softIrq", "stolen", "sys",
						 "total", "user", "wait")
				.values(cpu.getIdle(), cpu.getIrq(), cpu.getNice(),
						cpu.getSoftIrq(), cpu.getStolen(), cpu.getSys(),
						cpu.getTotal(), cpu.getUser(), cpu.getWait())
				.build();
	}

	private Serie toSeries(Mem mem) {
		return new Serie.Builder("mem")
				.columns("actualFree", "actualUsed", "free", "freePercent",
						 "ram", "total", "used", "usedPercent")
				.values(mem.getActualFree(), mem.getActualUsed(),
						mem.getFree(), mem.getFreePercent(), mem.getRam(),
						mem.getTotal(), mem.getUsed(), mem.getUsedPercent())
				.build();
	}

	private Serie toSeries(ProcStat procStat) {
		return new Serie.Builder("procStat")
				.columns("idle", "running", "sleeping", "stopped", "threads",
						"total", "zombie")
				.values(procStat.getIdle(), procStat.getRunning(),
						procStat.getSleeping(), procStat.getStopped(),
						procStat.getThreads(), procStat.getTotal(),
						procStat.getZombie()).build();
	}

	private Serie toSeries(Swap swap) {
		return new Serie.Builder("swap")
				.columns("free", "pageIn", "pageOut", "total", "used")
				.values(swap.getFree(), swap.getPageIn(), swap.getPageOut(), 
						swap.getTotal(), swap.getUsed())
				.build();
	}
}
