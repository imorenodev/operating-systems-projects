package com.luv2code.springdemo;

public class SoccerCoach implements Coach {

	// define a private field for the dependency
	private FortuneService fortuneService;

	// define a constructor for dependency injection
	public SoccerCoach(FortuneService theFortuneService) {
		fortuneService = theFortuneService;
	}

	@Override
	public String getDailyWorkout() {
		return "Do 5 suicides while dribbling the ball!";
	}

	@Override
	public String getDailyFortune() {
		return fortuneService.getFortune();
	}

}
