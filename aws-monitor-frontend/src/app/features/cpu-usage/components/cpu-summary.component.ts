import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cpu-summary',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="summary">
      <span class="label">Instance:</span>
      <span class="value">{{ instanceId }}</span>
    </div>
  `,
  styles: [`
    .summary {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 10px;
      color: #444;
      font-size: 13px;
    }
    .label { color: #666; }
    .value { font-weight: 600; }
  `]
})
export class CpuSummaryComponent {
  @Input({ required: true }) instanceId!: string;
}
