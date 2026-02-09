export interface CpuUsageVm {
  instanceId: string;
  series: Array<{ x: Date; y: number }>;
  statistic: string;
}
