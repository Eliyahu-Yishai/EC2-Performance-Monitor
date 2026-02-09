import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="app">
      <header class="header">
        <div class="brand">
          <div class="title">AWS Monitor</div>
          <!-- <div class="subtitle">CPU Usage over time</div> -->
        </div>
      </header>

      <main class="main">
        <router-outlet />
      </main>
    </div>
  `,
  styles: [`
    .app { min-height: 100vh; font-family: Arial; background: #fafafa; }
    .header { padding: 36px 24px;  }
    .brand { display: flex; flex-direction: column; gap: 4px; }
    .title { font-size: 25px; font-weight: 700; }
    .main { padding: 24px; }
  `]
})
export class App {}
