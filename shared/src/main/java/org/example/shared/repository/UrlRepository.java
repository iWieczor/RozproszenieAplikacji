package org.example.shared.repository;

import org.example.shared.model.UrlEntry;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UrlRepository extends CassandraRepository<UrlEntry, String> {
}
