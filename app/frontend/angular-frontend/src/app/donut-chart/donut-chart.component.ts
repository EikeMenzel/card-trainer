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

export class DonutChartComponent implements OnInit{
  @Input() public chartNames: string[] = []
  @Input() public chartValues: number[] = []
  public chart: any;

  ngOnInit(): void {
    this.chart = new Chart("doughnetChart", {
      type: 'doughnut',
      data: {
        labels: this.chartNames,
        datasets: [{
          label: 'Donut Chart',
          data: this.chartValues,
          backgroundColor: ["#9ef01a","#70e000","#38b000","#008000","#007200","#006400","#004b23"],
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
