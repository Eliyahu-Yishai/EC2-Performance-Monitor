export interface CpuQueryRequestDto {
  ip: string;
  minutesBack: number;
  intervalSeconds: number;
  statistic: string;
}