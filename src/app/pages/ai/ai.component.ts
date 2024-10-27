import { Component, OnInit } from '@angular/core';
import {IaService} from "../../services/ia.service";

@Component({
  selector: 'app-ai',
  templateUrl: './ai.component.html',
  styleUrls: ['./ai.component.scss']
})
export class AIComponent implements OnInit {
  sentences: string[] = [
    'analyse overdue missions and advices',
    'Company Future?'
  ];

  currentSentenceIndex: number = 0;
  index: number = 0;
  showDateInputs:boolean = false
  Description:any;
  ParticipantNumber:any;
  InfoGivven:boolean = false;
  isTyping:boolean = false;
  SalariesPrediction:any;
  lines:any;
  currentLineIndex:any;
  typingLine:any;
  typedLines:any;
  typedText: string = '';
  constructor(private ia_service:IaService) { }

  ngOnInit(): void {
    this.type();
  }

  type() {
    if (this.index < this.sentences[this.currentSentenceIndex].length) {
      this.typedText += this.sentences[this.currentSentenceIndex].charAt(this.index);
      this.index++;
      setTimeout(() => this.type(), 100);
    } else {
      setTimeout(() => this.untype(), 1000);
    }
  }

  untype() {
    if (this.typedText.length > 0) {
      this.typedText = this.typedText.slice(0, -1);
      setTimeout(() => this.untype(), 100);
    } else {
      this.currentSentenceIndex = (this.currentSentenceIndex + 1) % this.sentences.length;
      this.index = 0;
      setTimeout(() => this.type(), 500);
    }
  }
  showSalaryInputs() {
    this.showDateInputs = true;
  }

  extra_skills_needed_for_future_missions_with_advices_to_hire(){
    this.ia_service.extra_skills_needed_for_future_missions_with_advices_to_hire()
      .then(response => {
        const parsedData = response.response;
        this.InfoGivven = true;
        this.SalariesPrediction = parsedData;
        this.lines = parsedData.split('\n');
        // Start typing animation
        this.currentLineIndex = 0;
        this.typingLine = '';
        this.isTyping = true;
        this.typeLine();


      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

  analyse_overdue_missions_and_advices(){
    this.ia_service.analyse_overdue_missions_and_advices()
      .then(response => {
        const parsedData = response.response;
        this.InfoGivven = true;
        this.SalariesPrediction = parsedData;
        this.lines = parsedData.split('\n');
        // Start typing animation
        this.currentLineIndex = 0;
        this.typingLine = '';
        this.isTyping = true;
        this.typeLine();


      })
      .catch(error => {
        console.error('Error:', error);
      });
  }
  submitDates() {
    if (this.Description && this.ParticipantNumber) {
      const formData = new FormData();
      formData.append('missiondescription', this.Description);
      formData.append('participantsnumber', this.ParticipantNumber);
      this.ia_service.decide_Mission_Participants_and_Budget(formData)
        .then(response => {
          const parsedData = response.response;
          this.InfoGivven = true;
          this.SalariesPrediction = parsedData;
          this.lines = parsedData.split('\n');

          // Start typing animation
          this.currentLineIndex = 0;
          this.typingLine = '';
          this.isTyping = true;
          this.typeLine();


        })
        .catch(error => {
          console.error('Error:', error);
        });

    } else {
      console.error('Please select both start and end dates.');
    }
  }

  typeLine() {
    if (this.currentLineIndex < this.lines.length) {
      const currentLine = this.lines[this.currentLineIndex];

      if (this.typingLine.length < currentLine.length) {

        this.typingLine += currentLine.charAt(this.typingLine.length);
        setTimeout(() => this.typeLine(), 50);
      } else {

        this.typedLines.push(this.typingLine);
        this.typingLine = '';
        this.currentLineIndex++;

        setTimeout(() => {
          this.typeLine();
        }, 1000);
      }
    } else {
      this.isTyping = false;
    }
  }

  splitLine(line: string): string[] {

    return line.split(/(\s+)/);
  }


  isNumber(part: string): string {
    const trimmedPart = part.trim();
    const numberValue = Number(trimmedPart.replace(/[$,.*]/g, ''));


    if (!isNaN(numberValue) && trimmedPart.length > 0 && trimmedPart !== '-') {
      if (numberValue.toString().length > 2)
        return numberValue > 0 ? 'highlight-number' : 'highlight-number-negative';
    }

    return '';
  }

  resetState() {
    this.SalariesPrediction = null;  // Clear SalariesPrediction
    this.InfoGivven = false;  // Reset info flag
    this.lines = [];  // Clear lines
    this.currentLineIndex = 0;  // Reset current line index
    this.typingLine = '';  // Clear typing line
    this.isTyping = false;  // Reset typing flag
    this.typedLines = [];  // Clear typed lines
  }

}
