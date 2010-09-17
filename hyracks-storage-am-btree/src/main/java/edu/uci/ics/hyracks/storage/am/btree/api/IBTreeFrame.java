/*
 * Copyright 2009-2010 by The Regents of the University of California
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License from
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.hyracks.storage.am.btree.api;

import java.nio.ByteBuffer;

import edu.uci.ics.hyracks.api.dataflow.value.ISerializerDeserializer;
import edu.uci.ics.hyracks.api.exceptions.HyracksDataException;
import edu.uci.ics.hyracks.dataflow.common.data.accessors.ITupleReference;
import edu.uci.ics.hyracks.storage.am.btree.impls.MultiComparator;
import edu.uci.ics.hyracks.storage.am.btree.impls.SpaceStatus;
import edu.uci.ics.hyracks.storage.am.btree.impls.SplitKey;
import edu.uci.ics.hyracks.storage.common.buffercache.ICachedPage;

public interface IBTreeFrame {
	public void setPage(ICachedPage page);
	public ICachedPage getPage();
	public ByteBuffer getBuffer();
	
	public void insert(ITupleReference tuple, MultiComparator cmp) throws Exception;
	public void update(int rid, ITupleReference tuple) throws Exception;
	public void delete(ITupleReference tuple, MultiComparator cmp, boolean exactDelete) throws Exception;
	
	public void compact(MultiComparator cmp);
	public boolean compress(MultiComparator cmp) throws Exception;
	
	public void initBuffer(byte level);
	
	public int getTupleCount();
		
	// assumption: page must be write-latched at this point
	public SpaceStatus hasSpaceInsert(ITupleReference tuple, MultiComparator cmp);
	public SpaceStatus hasSpaceUpdate(int rid, ITupleReference tuple, MultiComparator cmp);
	
	public int getTupleOffset(int slotNum);
	
	public int getTotalFreeSpace();	
	
	public void setPageLsn(int pageLsn);
	public int getPageLsn();
	
	// for debugging
	public void printHeader();
	public String printKeys(MultiComparator cmp, ISerializerDeserializer[] fields) throws HyracksDataException;
	
	
	// TODO; what if tuples more than half-page size?
	public int split(IBTreeFrame rightFrame, ITupleReference tuple, MultiComparator cmp, SplitKey splitKey) throws Exception;		
	
	// TODO: check if we do something nicer than returning object
	public ISlotManager getSlotManager();
	
	// ATTENTION: in b-tree operations it may not always be possible to determine whether an ICachedPage is a leaf or interior node
	// a compatible interior and leaf implementation MUST return identical values when given the same ByteBuffer for the functions below	
	public boolean isLeaf();
	public byte getLevel();
	public void setLevel(byte level);	
	public boolean getSmFlag(); // structure modification flag
	public void setSmFlag(boolean smFlag);	
	
	public void insertSorted(ITupleReference tuple, MultiComparator cmp) throws Exception;
	
	public int getSlotSize();
	
	public IBTreeTupleReference createTupleReference();
	
	// TODO: should be removed after new tuple format		
	public void setPageTupleFieldCount(int fieldCount);
	
	// for debugging
	public int getFreeSpaceOff();
	public void setFreeSpaceOff(int freeSpace);
	
	
}
