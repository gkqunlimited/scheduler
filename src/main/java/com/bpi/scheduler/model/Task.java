package com.bpi.scheduler.task;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration; // in days

    @ManyToMany
    private List<Task> dependencies = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    public Task() {}

    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public void addDependency(Task task) {
        dependencies.add(task);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void calculateDates() {
        if (dependencies.isEmpty()) {
            startDate = new Date(); // Start today if no dependencies
        } else {
            Date maxEndDate = dependencies.get(0).getEndDate();
            for (Task dependency : dependencies) {
                if (dependency.getEndDate().after(maxEndDate)) {
                    maxEndDate = dependency.getEndDate();
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(maxEndDate);
            cal.add(Calendar.DAY_OF_MONTH, 1); // Start the next day
            startDate = cal.getTime();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_MONTH, duration);
        endDate = cal.getTime();
    }
}
