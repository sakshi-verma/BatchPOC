package hello;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.User;
import hello.UserRepository;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

@Controller
@RequestMapping(path="/demo")
public class MainController {
	@Autowired
	private UserRepository userRepository;
/*	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;*/
	@Autowired
	private BatchConfiguration batchConfiguration;
	@Autowired
	private UserBatch userBatch;

	@GetMapping(path = "/add") // Map ONLY GET Requests
	public
	@ResponseBody
	String addNewUser(@RequestParam String name
			, @RequestParam String email) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		User n = new User();
		n.setName(name);
		n.setEmail(email);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path = "/all")
	public
	@ResponseBody
	Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}

	@GetMapping(path = "/runBatch1")
	public void runPersonBatch() throws Exception{
			batchConfiguration.runBatch();
	}

	@GetMapping(path = "/runBatch2")
	public void runUserBatch() throws Exception{
		userBatch.runBatch();
	}
}
