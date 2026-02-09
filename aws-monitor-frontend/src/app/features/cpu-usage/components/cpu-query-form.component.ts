import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { CpuQueryRequestDto } from '../models/cpu-query-request.dto';

@Component({
  selector: 'app-cpu-query-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="toolbar">
      <div class="left">
        <div class="metric">
          <span class="metric-name">CPU Utilization</span>
          <span class="metric-unit">( Percent )</span>
        </div>
      </div>

      <div class="right" [formGroup]="form">
        <label class="ctrl">
          Statistic:
          <select formControlName="statistic">
            <option value="Average">Average</option>
          </select>
        </label>

        <label class="ctrl">
          Time Range:
          <select formControlName="minutesBack">
            <option [ngValue]="15">Last 15 Minutes</option>
            <option [ngValue]="60">Last Hour</option>
            <option [ngValue]="180">Last 3 Hours</option>
            <option [ngValue]="720">Last 12 Hours</option>
            <option [ngValue]="1440">Last Day</option>
          </select>
        </label>

        <label class="ctrl">
          Period:
          <select formControlName="intervalSeconds">
            <option [ngValue]="300">5 Minutes</option>
            <option [ngValue]="900">15 Minutes</option>
          </select>
        </label>

        <label class="ctrl ip">
          IP:
          <input formControlName="ip" placeholder="1.2.3.4" />
        </label>

        <button class="btn" (click)="submit()" [disabled]="loading || form.invalid">
         <div class="btnText">Refresh</div> 
        </button>
      </div>
    </div>

    <div class="validation-errors">
      <div class="hint" *ngIf="form.errors?.['periodTooLarge']">
        ⚠️ Selected period must be smaller than the selected time range.
      </div>
    </div>
  `,
  styles: [`
    .toolbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 28px;
      padding: 19px 18px;
      border-bottom: 1px solid #e5e5e5;
      background: #f1f1f1;
      flex-wrap: wrap;
    }
    .metric { 
      font-weight: 600; 
    }
    .metric-unit { 
      font-weight: 400; color: #666; margin-left: 6px;
     }
    .right {
       display: flex; align-items: center; gap: 10px; flex-wrap: wrap;
       }
    .ctrl { 
      display: flex; align-items: center; gap: 6px; font-size: 18px; white-space: nowrap; 
    }
    select, input {
      height: 28px;
      border: 1px solid #cfcfcf;
      border-radius: 4px;
      padding: 0 8px;
      font-size: 18px;
      background: #fff;
    }
    .ip input { width: 150px; }
    .btn {
      height: 30px;
      padding: 0 12px;
      font-size: 18px
      border: 1px solid #bdbdbd;
      background: #fff;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn:disabled { 
      opacity: 1; cursor: not-allowed;
     }
    .btnText {
      font-size: 18px
    }
    .validation-errors {
      position: relative;
      // padding: 0 16px;
    }
    .hint {
      position: absolute;
      right: 0;
      padding: 10px 24px;
      font-size: 17px;
      color: #000000;
    }
  `]
})

export class CpuQueryFormComponent {
  @Input() loading = false;
  @Output() run = new EventEmitter<CpuQueryRequestDto>();
  @Output() formChange = new EventEmitter<void>();

  readonly form: ReturnType<FormBuilder['group']>;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      ip: ['', [Validators.required]],
      minutesBack: [60, [Validators.required]],
      intervalSeconds: [300, [Validators.required]],
      statistic: ['Average', [Validators.required]],
    }, { validators: this.periodValidator });

    this.form.valueChanges.subscribe(() => {
      this.formChange.emit();
    });
  }

  private periodValidator(control: AbstractControl): ValidationErrors | null {
    const minutesBack = control.get('minutesBack')?.value;
    const intervalSeconds = control.get('intervalSeconds')?.value;

    if (!minutesBack || !intervalSeconds) {
      return null;
    }

    const timeRangeSeconds = minutesBack * 60;

    if (intervalSeconds >= timeRangeSeconds) {
      return { periodTooLarge: true };
    }

    return null;
  }

  submit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const v = this.form.getRawValue();
    const req: CpuQueryRequestDto = {
      ip: v.ip!,
      minutesBack: v.minutesBack!,
      intervalSeconds: v.intervalSeconds!,
      statistic: v.statistic!,
    };
    this.run.emit(req);
  }
}
