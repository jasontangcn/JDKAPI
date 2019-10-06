package com.fairchild.jdkapi.jdbc;

public class ConnectionStatementtResultProperties {
	private int CONNECTION_TRANSACTION_ISOLATIONLEVEL = -1;
	private int STATEMENT_CURRENT = -1;
	private int STATEMENT_GENERATEKEYS = -1;
	private int RESULTSET_TYPE = -1;
	private int RESULTSET_CONCURRENCY = -1;
	private int RESULTSET_HOLDABILITY = -1;

	public ConnectionStatementtResultProperties() {
	}

	public ConnectionStatementtResultProperties(int transactionIsolationLevel, int statementCurrent, int statementGenerateKeys, int resultSetType, int resultSetConcurrency, int resultsetHoldAbility) {
		this.CONNECTION_TRANSACTION_ISOLATIONLEVEL = transactionIsolationLevel;
		this.STATEMENT_CURRENT = statementCurrent;
		this.STATEMENT_GENERATEKEYS = statementGenerateKeys;
		this.RESULTSET_TYPE = resultSetType;
		this.RESULTSET_CONCURRENCY = resultSetConcurrency;
		this.RESULTSET_HOLDABILITY = resultsetHoldAbility;
	}

	public void setConnectionTransactionIsolationLevel(int transactionIsolationLevel) {
		this.CONNECTION_TRANSACTION_ISOLATIONLEVEL = transactionIsolationLevel;
	}

	public int getConnectionTransactionIsolationLevel() {
		return this.CONNECTION_TRANSACTION_ISOLATIONLEVEL;
	}

	public void setStatementCurrent(int statementCurrent) {
		this.STATEMENT_CURRENT = statementCurrent;
	}

	public int getStatementCurrent() {
		return this.STATEMENT_CURRENT;
	}

	public void setStatementGenerateKeys(int statementGenerateKeys) {
		this.STATEMENT_GENERATEKEYS = statementGenerateKeys;
	}

	public int getStatementGenerateKeys() {
		return this.STATEMENT_GENERATEKEYS;
	}

	public void setResultSetType(int resultSetType) {
		this.RESULTSET_TYPE = resultSetType;
	}

	public int getResultSetType() {
		return this.RESULTSET_TYPE;
	}

	public void setResultsetConcurrency(int resultSetConcurrency) {
		this.RESULTSET_CONCURRENCY = resultSetConcurrency;
	}

	public int getResultsetConcurrency() {
		return this.RESULTSET_CONCURRENCY;
	}

	public void setResultsetHoldAbility(int resultsetHoldAbility) {
		this.RESULTSET_HOLDABILITY = resultsetHoldAbility;
	}

	public int getResultsetHoldAbility() {
		return this.RESULTSET_HOLDABILITY;
	}
}
