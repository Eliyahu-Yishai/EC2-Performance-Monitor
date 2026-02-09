import { Component, Input, OnChanges, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js';

import 'chartjs-adapter-date-fns';

// Register Chart.js components
Chart.register(...registerables);

@Component({
  selector: 'app-cpu-usage-chart',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  template: `
    <div class="chart-wrap">
      <canvas
        baseChart
        [type]="type"
        [data]="data"
        [options]="options">
      </canvas>
    </div>
  `,
  styles: [`
    .chart-wrap {
      height: 380px;
      border: 1px solid #eee;
      border-radius: 6px;
      padding: 10px;
      background: #fff;
    }
  `]
})
export class CpuUsageChartComponent implements OnChanges {
  @Input({ required: true }) instanceId!: string;
  @Input({ required: true }) series!: Array<{ x: Date; y: number }>;
  @Input({ required: true }) statistic!: string;

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  readonly type = 'line' as const;

  data: ChartConfiguration<'line'>['data'] = {
    datasets: [
      {
        label: '',
        data: [],
        tension: 0,         
        pointRadius: 3,
        pointHoverRadius: 4,
        borderWidth: 2,
        fill: false
      }
    ]
  };

  options: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: true, position: 'bottom' },
      tooltip: {
        callbacks: {
          label: (ctx) => ` ${ctx.parsed.y?.toFixed(2)}%`
        }
      }
    },
    scales: {
      x: {
        type: 'time',
        time: {
        },
        ticks: { maxRotation: 0 },
        grid: { display: true }
      },
      y: {
        beginAtZero: true,
        max: 100,
        ticks: {
          callback: (v) => `${v}`
        },
        grid: { display: true }
      }
    }
  };

  ngOnChanges(): void {
    const label = `${this.instanceId} (${this.statistic})`;
    const yMax = this.computeYAxisMax(this.series ?? []);

    this.data = {
      datasets: [
        {
          ...this.data.datasets[0],
          label,
          data: (this.series ?? []) as any
        }
      ]
    };

    setTimeout(() => {
      if (this.chart?.chart?.options?.scales?.['y']) {
        (this.chart.chart.options.scales['y'] as any).max = yMax;
        this.chart.update();
      }
    }, 0);
  }

  private computeYAxisMax(series: Array<{ x: Date; y: number }>): number {
    // If series is empty, default to 100
    if (!series || series.length === 0) {
      return 100;
    }

    // Find max value from all y values (filter out null/undefined)
    const validValues = series
      .map(point => point.y)
      .filter(y => y != null && typeof y === 'number');

    if (validValues.length === 0) {
      return 100;
    }

    const maxValue = Math.max(...validValues);

    // Calculate yMax with 1.5x buffer
    let yMax = maxValue * 1.5;

    // Clamp to range [5, 100]
    yMax = Math.max(5, Math.min(100, yMax));

    // Round UP to nearest multiple of 5
    yMax = Math.ceil(yMax / 5) * 5;

    return yMax;
  }
}
