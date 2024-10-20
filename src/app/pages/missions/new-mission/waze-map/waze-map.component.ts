import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";

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


  private updateMapUrl() {
    if (this.isValidCoordinates(this.lat, this.lon)) {
      const url = `https://embed.waze.com/iframe`;
      this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    } else {
      console.error('Invalid latitude or longitude');
      // Handle invalid coordinates (e.g., show a default map or a message)
    }
  }

  private isValidCoordinates(lat: number, lon: number): boolean {
    return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
  }

  ngOnInit(): void {
    this.updateMapUrl();
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

}
