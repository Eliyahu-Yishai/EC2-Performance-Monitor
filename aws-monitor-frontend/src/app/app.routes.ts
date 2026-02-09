import { Routes } from '@angular/router';
import { CPU_USAGE_ROUTES } from './features/cpu-usage/cpu-usage.routes';

export const routes: Routes = [
  { path: '', redirectTo: 'cpu', pathMatch: 'full' },
  { path: 'cpu', children: CPU_USAGE_ROUTES },
  { path: '**', redirectTo: 'cpu' }
];
