import { CpuPointDto } from "./cpu-point.dto";

export interface CpuSeriesResponseDto {
  instanceId: string;
  points: CpuPointDto[];
}