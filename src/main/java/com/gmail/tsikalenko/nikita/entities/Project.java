package com.gmail.tsikalenko.nikita.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager")
    private Manager manager;
    private String status;
    @Column(name = "start_date")
    private String startDate;
    private String deadline;
}