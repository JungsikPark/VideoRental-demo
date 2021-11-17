package video.rental.demo.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Rental;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class Interactor {
	private Repository repository;
	private List<IUIObserver> uiObservers = new ArrayList<IUIObserver>();
	
	public Interactor(Repository repository) {
		super();
		this.repository = repository;
	}

	public void clearRentals(int customerCode) {
		StringBuilder builder = new StringBuilder();
		
		Customer foundCustomer = repository.findCustomerById(customerCode);
	
		if (foundCustomer == null) {
			builder.append("No customer found\n");
		} else {
			builder.append("Id: " + foundCustomer.getCode() + "\nName: " + foundCustomer.getName() + "\tRentals: "
					+ foundCustomer.getRentals().size() + "\n");
			for (Rental rental : foundCustomer.getRentals()) {
				builder.append("\tTitle: " + rental.getVideo().getTitle() + " ");
				builder.append("\tPrice Code: " + rental.getVideo().getPriceCode());
			}
	
			foundCustomer.clearRental();
	
			repository.saveCustomer(foundCustomer);
		}
		
		notify(builder.toString());
	}

	public void returnVideo(int customerCode, String videoTitle) {
		Customer foundCustomer = repository.findCustomerById(customerCode);
		if (foundCustomer == null)
			return;
	
		List<Rental> customerRentals = foundCustomer.getRentals();
	
		for (Rental rental : customerRentals) {
			if (rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented()) {
				Video video = rental.returnVideo();
				video.setRented(false);
				repository.saveVideo(video);
				break;
			}
		}
	
		repository.saveCustomer(foundCustomer);
	}

	public void listVideos() {
		StringBuilder builder = new StringBuilder();
		
		List<Video> videos = repository.findAllVideos();
	
		for (Video video : videos) {
			builder.append(
					"Video type: " + video.getVideoType() + 
					"\tPrice code: " + video.getPriceCode() + 
					"\tRating: " + video.getVideoRating() +
					"\tTitle: " + video.getTitle() + "\n"
					); 
		}
		notify(builder.toString());
	}

	public void listCustomers() {
		StringBuilder builder = new StringBuilder();
		List<Customer> customers = repository.findAllCustomers();
	
		for (Customer customer : customers) {
			builder.append("ID: " + customer.getCode() + "\nName: " + customer.getName() + "\tRentals: "
					+ customer.getRentals().size() + "\n");
			for (Rental rental : customer.getRentals()) {
				builder.append("\tTitle: " + rental.getVideo().getTitle() + " ");
				builder.append("\tPrice Code: " + rental.getVideo().getPriceCode());
				builder.append("\tReturn Status: " + rental.getStatus() + "\n");
			}
		}
		notify(builder.toString());
	}

	public void getCustomerReposrt(int code) {
		StringBuilder builder = new StringBuilder();
		Customer foundCustomer = repository.findCustomerById(code);
	
		if (foundCustomer == null) {
			builder.append("No customer found\n");
		} else {
			String result = foundCustomer.getReport();
			builder.append(result).append("\n");
		}
		notify(builder.toString());
	}

	public void rentVideo(int code, String videoTitle) {
		Customer foundCustomer = repository.findCustomerById(code);
		if (foundCustomer == null)
			return;
	
		Video foundVideo = repository.findVideoByTitle(videoTitle);
	
		if (foundVideo == null)
			return;
	
		if (foundVideo.isRented() == true)
			return;
	
		Boolean status = foundVideo.rentFor(foundCustomer);
		if (status == true) {
			repository.saveVideo(foundVideo);
			repository.saveCustomer(foundCustomer);
		} else {
			return;
		}
	}

	public void registerCustomer(String name, int code, String dateOfBirth) {
		Customer customer = new Customer(code, name, LocalDate.parse(dateOfBirth));
		repository.saveCustomer(customer);
	}

	public void registerVideo(String title, int videoType, int priceCode, int videoRating, LocalDate registeredDate) {
		Rating rating;
		if (videoRating == 1) rating = Rating.TWELVE;
		else if (videoRating == 2) rating = Rating.FIFTEEN;
		else if (videoRating == 3) rating = Rating.EIGHTEEN;
		else throw new IllegalArgumentException("No such rating " + videoRating);
		
		Video video = new Video(title, videoType, priceCode, rating, registeredDate);
	
		repository.saveVideo(video);
	}
	
	public void addUIObserver(IUIObserver observer) {
		uiObservers.add(observer);
	}
	
	public void removeUIObserver(IUIObserver observer) {
		uiObservers.remove(observer);
	}
	
	public void notify(String message) {
		for(IUIObserver observer : uiObservers) {
			observer.update(message);
		}
	}
}
