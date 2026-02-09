import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CpuUsageFacade } from '../state/cpu-usage.facade';
import { CpuQueryRequestDto } from '../models/cpu-query-request.dto';
import { CpuQueryFormComponent } from '../components/cpu-query-form.component';
import { CpuUsageChartComponent } from '../components/cpu-usage-chart.component';
import { CpuSummaryComponent } from '../components/cpu-summary.component';

@Component({
  selector: 'app-cpu-usage-page',
  standalone: true,
  imports: [CommonModule, CpuQueryFormComponent, CpuUsageChartComponent, CpuSummaryComponent],
  template: `
    <div class="page">
      <div class="card">
        <!-- <div class="header">
          <div class="title">CloudWatch Monitoring Details</div>
        </div> -->

        <app-cpu-query-form
          (run)="onRun($event)"
          (formChange)="onFormChange()"
          [loading]="loading()"
        />

        <div class="content">
          <div class="status" *ngIf="error() as e">
            {{ e }}
          </div>

          <app-cpu-summary
            *ngIf="vm() as data"
            [instanceId]="data.instanceId"
          />

          <app-cpu-usage-chart
            *ngIf="vm() as data"
            [instanceId]="data.instanceId"
            [series]="data.series"
            [statistic]="data.statistic"
          />

          <div class="loading" *ngIf="loading()">
            Loading...
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page { 
      padding: 24px; font-family: Arial, sans-serif; 
    }
    .card {
       border: 1px solid #ddd; border-radius: 8px; overflow: hidden; 
      }
    .header {
       padding: 12px 16px; background: #f7f7f7; border-bottom: 1px solid #e5e5e5;
       }
    .title {
       font-size: 22px; font-weight: 600; 
      }
    .content { 
      padding: 16px; 
    }
    .status {
      padding: 10px 12px;
      background: #fff3f3;
      border: 1px solid #ffd1d1;
      border-radius: 6px;
      margin-bottom: 12px;
      font-size: 17px;
      color: #000000;
    }
    .loading { 
      margin-top: 12px; color: #666;
     }
  `]
})
export class CpuUsagePageComponent {
  constructor(private facade: CpuUsageFacade) {}

  get loading() { return this.facade.loading; }
  get error() { return this.facade.error; }
  get vm() { return this.facade.vm; }

  onFormChange() {
    this.facade.clearError();
  }

  onRun(req: CpuQueryRequestDto) {
    this.facade.load(req);
  }
}
