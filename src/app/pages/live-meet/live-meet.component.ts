import {Component, ElementRef, OnInit} from '@angular/core';
import {ZegoUIKitPrebuilt} from "@zegocloud/zego-uikit-prebuilt";
import {Router} from "@angular/router";

@Component({
  selector: 'app-live-meet',
  templateUrl: './live-meet.component.html',
  styleUrls: ['./live-meet.component.scss']
})
export class LiveMeetComponent implements OnInit {

  // private appId: number = 1711941085; // Replace with your own app ID
  // private tokenServerUrl: string = 'https://nextjs-token.vercel.app/api';
  //
  // constructor(private elementRef: ElementRef) {}
  //
  // ngOnInit(): void {
  //   this.init();
  // }
  //
  // private async generateToken(tokenServerUrl: string, userID: string) {
  //   const response = await fetch(`${tokenServerUrl}/access_token?userID=${userID}&expired_ts=7200`);
  //   return response.json();
  // }
  //
  // private randomID(len: number): string {
  //   const chars = '12345qwertyuiopasdfgh67890jklmnbvcxzMNBVCZXASDQWERTYHGFUIOLKJP';
  //   let result = '';
  //   for (let i = 0; i < len; i++) {
  //     result += chars.charAt(Math.floor(Math.random() * chars.length));
  //   }
  //   return result;
  // }
  //
  // private getUrlParams(url: string) {
  //   const urlStr = url.split('?')[1];
  //   const urlSearchParams = new URLSearchParams(urlStr);
  //   return Object.fromEntries(urlSearchParams.entries());
  // }
  //
  // private async init() {
  //   const roomID = this.getUrlParams(window.location.href)['roomID'] || this.randomID(5);
  //   const userID = this.randomID(5);
  //   const userName = this.randomID(5);
  //   const { token } = await this.generateToken(this.tokenServerUrl, userID);
  //
  //   const kitToken = ZegoUIKitPrebuilt.generateKitTokenForProduction(
  //     this.appId,
  //     token,
  //     roomID,
  //     userID,
  //     userName
  //   );
  //
  //   const zp = ZegoUIKitPrebuilt.create(kitToken);
  //
  //   zp.joinRoom({
  //     container: this.elementRef.nativeElement.querySelector('#app'),
  //     maxUsers: 4,
  //     branding: {
  //       logoURL: 'https://www.zegocloud.com/_nuxt/img/zegocloud_logo_white.ddbab9f.png',
  //     },
  //     scenario: {
  //       mode: ZegoUIKitPrebuilt.GroupCall,
  //     },
  //     sharedLinks: [
  //       {
  //         name: 'Personal link',
  //         url: window.location.origin + window.location.pathname + '?roomID=' + roomID,
  //       },
  //     ],
  //   });
  // }


  constructor(private router:Router) {
  }
  ngOnInit() {
  }
  RoomId:any;
  EnterRoom(){
    this.router.navigate([`/LiveMeet/Room/${this.RoomId}`])
    console.log(this.RoomId)
  }
}
