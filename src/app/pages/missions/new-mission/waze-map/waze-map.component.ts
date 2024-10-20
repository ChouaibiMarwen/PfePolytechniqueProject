import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import * as url from "node:url";

@Component({
  selector: 'app-waze-map',
  templateUrl: './waze-map.component.html',
  styleUrls: ['./waze-map.component.scss']
})
export class WazeMapComponent  implements OnInit, OnChanges {
  @Input() lat: number = 37.7749; // Default latitude
  @Input() lon: number = -122.4194; // Default longitude
  @Input() zoom: number = 14; // Default zoom level


  safeUrl!: SafeResourceUrl;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnChanges() {
    const url = `https://embed.waze.com/iframe?zoom=${this.zoom}&lat=${this.lat}&lon=${this.lon}&pin=1`;
    this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  ngOnInit(): void {
  }

}
