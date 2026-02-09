import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CpuQueryRequestDto } from '../models/cpu-query-request.dto'
import { CpuSeriesResponseDto } from '../models/cpu-series-response.dto'
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CpuUsageApiClient {
  constructor(private http: HttpClient) {}

  getCpuUsage(req: CpuQueryRequestDto): Observable<CpuSeriesResponseDto> {
    return this.http.post<CpuSeriesResponseDto>('/api/cpu/usage', req);
  }
}
