
package com.example.demo.repisotory;

import com.example.demo.models.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ClaimsRepository extends JpaRepository<Claim, Integer> {

    public Claim findByInvoiceNumber(String invoiceNumber);

}
