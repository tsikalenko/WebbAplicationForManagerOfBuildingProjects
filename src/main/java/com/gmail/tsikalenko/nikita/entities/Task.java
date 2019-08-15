package com.gmail.tsikalenko.nikita.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="project")
    private Project project;
    private String description;
    private String status;
    @Column(name = "start_date")
    private String startDate;
    private String deadline;
    private String priority;
    private String materials;
    private String performer;
    private Long costs;
}
