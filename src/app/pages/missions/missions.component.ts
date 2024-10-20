import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {MissionService} from "../../services/mission.service";
import {Missions, PaginatedMissions} from "../../interfaces/missions";

@Component({
  selector: 'app-missions',
  templateUrl: './missions.component.html',
  styleUrls: ['./missions.component.scss']
})
export class MissionsComponent implements OnInit {
  paginatedMissions: PaginatedMissions = {size: 5, nb_elements: 0, page: -1, elements: [], pages: 1}
  idTeamLead: any;
  startdate: any;
  enddate: any;
  status: any = null;
  title: any;
  constructor(private router: Router,private User:UserService,private mission:MissionService) { }
  ngOnInit(): void {
    this.getAllMissions(0)
  }




  NewMission(){
    this.router.navigate(['/missions/add'])
  }


  statusOptions: { value: string, label: string }[] = [
    { value: 'CANCELLED', label: 'Cancelled' },
    { value: 'COMPLETED', label: 'Completed' },
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'OVERDUE', label: 'Overdue' },
    { value: 'PENDING', label: 'Pending' }
  ];

  getAllMissions(page: any) {
    this.mission.getMissions(page,this.paginatedMissions.size,this.idTeamLead,this.startdate,this.enddate,this.status,this.title).then((res) => {
      this.paginatedMissions.nb_elements = res.totalItems
      this.paginatedMissions.page = res.currentPage
      this.paginatedMissions.pages = res.totalPages
      this.paginatedMissions.elements = res.result
      console.log(this.paginatedMissions.elements)
    }).finally(() => {
    })
  }


  selectedMission: any = {
    id: null,
    participants: []
  };

  openUpdateModal(missionId: number, participants: any[]) {
    this.selectedMission.id = missionId;
    this.selectedMission.participants = participants;

    const modal = document.getElementById('updateParticipantsModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
      document.body.classList.add('modal-open');

      // Create backdrop
      const backdrop = document.createElement('div');
      backdrop.className = 'modal-backdrop fade show';
      document.body.appendChild(backdrop);

      backdrop.onclick = () => this.closeModal(modal, backdrop);
    }
  }

  closeModal(modal: HTMLElement, backdrop: HTMLElement) {
    modal.classList.remove('show');
    modal.style.display = 'none';
    document.body.classList.remove('modal-open');
    document.body.removeChild(backdrop);
  }

  saveParticipants() {
    // Logic to save the selected participants
    console.log('Updated participants for mission:', this.selectedMission.id, this.selectedMission.participants);

    // Close the modal after saving
    const modal = document.getElementById('updateParticipantsModal') as HTMLElement;
    const backdrop = document.querySelector('.modal-backdrop') as HTMLElement;
    if (modal && backdrop) {
      this.closeModal(modal, backdrop);
    }
  }

}
