import { Injectable, signal, computed } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CpuUsageApiClient } from '../api/cpu-usage-api.client';
import { CpuQueryRequestDto } from '../models/cpu-query-request.dto';
import { CpuUsageVm } from '../models/cpu-usage.vm';

@Injectable({ providedIn: 'root' })
export class CpuUsageFacade {
  private loadingSig = signal(false);
  private errorSig = signal<string | null>(null);
  private vmSig = signal<CpuUsageVm | null>(null);

  loading = computed(() => this.loadingSig());
  error = computed(() => this.errorSig());
  vm = computed(() => this.vmSig());

  constructor(private api: CpuUsageApiClient) {}

  clearError(): void {
    this.errorSig.set(null);
  }

  load(req: CpuQueryRequestDto): void {
    this.loadingSig.set(true);
    this.errorSig.set(null);

    this.api.getCpuUsage(req).subscribe({
      next: (res) => {
        const points = res.points ?? [];
        const series = points
          .map(p => ({ x: new Date(p.timestamp), y: p.value }))
          .sort((a, b) => a.x.getTime() - b.x.getTime());

        this.vmSig.set({
          instanceId: res.instanceId,
          series,
          statistic: req.statistic
        });

        this.loadingSig.set(false);
      },
      error: (err) => {
        this.errorSig.set(this.toUserMessage(err));
        this.loadingSig.set(false);
      }
    });
  }

  private toUserMessage(err: unknown): string {
    if (!(err instanceof HttpErrorResponse)) return 'Something went wrong.';

    if (err.status === 0) return 'Cannot reach server.';
    if (err.status === 400) return 'Bad request. Please check your input.';
    if (err.status === 404) return 'Instance not found / no data.';
    if (err.status >= 500) return 'Server error. Try again later.';

    return 'Unexpected error.';
  }
}
