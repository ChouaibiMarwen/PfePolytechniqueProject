import {AfterViewInit, Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ZegoUIKitPrebuilt} from "@zegocloud/zego-uikit-prebuilt";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit, AfterViewInit {
  private router = inject(ActivatedRoute)

  constructor() {
  }

  @ViewChild('root')
  root!: ElementRef;
  roomID: string = ""

  ngOnInit(): void {
    this.router.params.subscribe((param) => {
      this.roomID = param['roomID'];
    })
  }

  ngAfterViewInit() {
    const appID = 1711941085;
    const serverSecret = "634ec4b03925480e57a91e47743a2e5b";

    const kitToken = ZegoUIKitPrebuilt.generateKitTokenForTest(
      appID,
      serverSecret,
      this.roomID,
      Date.now().toString(),
      Date.now().toString()
    );

    // Create instance object from Kit Token.
    const zp = ZegoUIKitPrebuilt.create(kitToken);

    zp.joinRoom({
      container: this.root.nativeElement,
      sharedLinks: [
        {
          name: 'Personal link',
          url:
            window.location.protocol + '//' +
            window.location.host + window.location.pathname +
            '?roomID=' +
            this.roomID,
        },
      ],
      scenario: {
        mode: ZegoUIKitPrebuilt.GroupCall, // To implement 1-on-1 calls, modify the parameter here to [ZegoUIKitPrebuilt.OneONoneCall].
      },
    });
  }

}
