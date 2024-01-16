import {AfterViewInit, Component, ElementRef, Input, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Chart, registerables} from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-donut-chart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './donut-chart.component.html',
  styleUrl: './donut-chart.component.css'
})

export class DonutChartComponent implements OnInit {
  @Input() public chartNames: string[] = []
  @Input() public chartValues: number[] = []
  @Input() public chartColors: string[] = []
  public chart: any;


  ngOnInit(): void {
    if (this.chartColors.length == 0) {
      this.chartColors = ["#c4c4c4", "#cce5ff", "#ccffcc", "#fff2cc", "#FAC898", "#FFB8A9", "#E96954"];
    }
    this.chart = new Chart("doughnetChart", {
      type: 'doughnut',
      data: {
        labels: this.chartNames,
        datasets: [{
          label: 'Donut Chart',
          data: this.chartValues,
          backgroundColor: this.chartColors,
          borderWidth: (this.chartValues == null) ? 3 : 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom'
          }
        },
      }
    });
  }
}
