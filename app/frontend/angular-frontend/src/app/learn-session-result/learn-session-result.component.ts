import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-learn-session-result',
  templateUrl: './learn-session-result.component.html',
  styleUrls: ['./learn-session-result.component.css']
})
export class LearnSessionResultComponent {
  // Define any properties that you need to interpolate into your template here.
  // For example, if you're displaying the number of cards guessed correctly, you'd define it like this:
  cardsCorrectlyGuessed = 10;

  // Include any methods for handling events here.
  // For example, if you need to handle the click event on your 'Back to Deck' button, you'd define it here.
  backToDeck() {
    // Handle navigation back to the deck overview.
  }

  // If you're using a chart library, you'd initialize your chart in the lifecycle hook that makes the most sense for your application.
  ngOnInit() {
    // Initialize your chart here.
  }
}
