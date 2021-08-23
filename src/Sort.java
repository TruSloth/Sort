import java.util.Scanner;

public class Sort {
    private int[] arr;

    public Sort() {
        // Get User Input for Array
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter number of Integer elements to be sorted:");
        int n = scan.nextInt();
        this.arr = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.printf("Enter integer value for element no. %d:", i + 1);
            this.arr[i] = scan.nextInt();
        }
        scan.close();
    }

    public void printArray() {
        System.out.println("Finally sorted array is: ");
        for (int i = 0; i < this.arr.length; i++) {
            System.out.printf("%d ", this.arr[i]);
        }
    }

    //CZ2002 Tutorial 1 Q3
    public void bubbleSort() {
        for (int i = this.arr.length - 2; i >= 0; i-- ) {
            for (int j = 0; j <= i; j++) {
                if (this.arr[j] > this.arr[j + 1]) {
                    this.arr[j] += this.arr[j + 1];
                    this.arr[j + 1] = this.arr[j] - this.arr[j + 1];
                    this.arr[j] -= this.arr[j + 1];
                }
            }
        }
    }

    public void insertionSort() {
        // Check for the trivial case where there is only 1 element
        if (this.arr.length == 1) {
            return;
        }

        /* 
         * Starting from 2nd element, check each element before it and insert it at position j + 1, the first position
         * such that the element at postion j <  it and the element at postion j + 2 > it. Maintains the loop invariant that
         * all elements in arr[0 .. j - 1] are sorted 
         */
        for (int i = 1; i < this.arr.length; i++) {
            int key = this.arr[i];
            int j = i - 1;
            
            while (j >= 0 && this.arr[j] > key) {
                this.arr[j + 1] = this.arr[j];
                j--;
            }

            this.arr[j + 1] = key;
        }
    }

    public void mergeSort() {
        // First call to mergeSort
        int start = 0;
        int end = this.arr.length - 1;
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(start, mid);
            mergeSort(mid + 1, end);
            merge(start, mid, end);
        }
    }

    public void mergeSort(int start, int end) {
        // Keep halving the array to create 2 subarrays, then recursively call mergeSort on the subarrays
        // Once the recursion bottoms out, we combine the various subarrays, maintaining the loop
        // invariant that the merged subarray is always sorted
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(start, mid);
            mergeSort(mid + 1, end);
            merge(start, mid, end);
        }
    }

    public void quickSort() {
        // First call to quickSort
        int start = 0;
        int end = this.arr.length - 1;
        if (start < end) {
            int partition = qSortPartition(start, end);
            quickSort(start, partition - 1);
            quickSort(partition + 1, end);
        }
    }

    public void quickSort(int start, int end) {
        // Run qSortPartition on the subarray, putting the item at end into its final position
        // and return this position as partition. Use this partition to split the subarray into
        // 2 more subarrays (excluding the item at partition), then recursively call quickSort on the subarrays.
        if (start < end) {
            int partition = qSortPartition(start, end);
            quickSort(start, partition - 1);
            quickSort(partition + 1, end);
        }
    }

    public void heapSort() {
        int heapSize = buildMaxHeap();
        for (int i = this.arr.length - 1; i >= 1; i--) {
            this.arr[i] += this.arr[0];
            this.arr[0] = this.arr[i] - this.arr[0];
            this.arr[i] -= this.arr[0];
            heapSize--;
            maxHeapify(0, heapSize);
        }
    }

    // Subroutines used for the sorting algorithms are found below
    private void mergeInPlace(int start, int mid, int end) {
        /*
         * Implementation for in-place merge
         * Complexity of O(n^2) due to the shifting process
         */
        int left = start;
        int right = mid + 1;
        
        // The last element of left subarray is already less than the first element of right subarray,
        // so the whole array is already sorted. No need to do anything.
        if (this.arr[mid] <= this.arr[right]) {
            return;
        }

        // Keep looping while there are elements in both subarrays
        while (left <= mid && right <= end) {
            // If the first element in the left subarray <= first element in right subarray, simply increment left pointer.
            if (this.arr[left] <= this.arr[right]) {
                left++;
            } else {
                /*
                 * The first element in the right subarray < first element in left subarray, 
                 * so first make a copy of arr[right], then shift all elements in arr[left .. right] by 1,
                 * before adding the copy to the position at left pointer. Finally, increment all pointers.
                 */
                int tmp = this.arr[right];
                for (int i = right; i > left; i--) {
                    this.arr[i] = this.arr[i - 1];
                }
                this.arr[left] = tmp;
                left++;
                right++;
                mid++;
            }
        }
    }

    private void merge(int start, int mid, int end) {
        /*
         * Standard implementation for merge.
         * Complexity of O(n). In total, standard merge sort has complexity of O(nlogn) due to the halving.
         * Time complexity proved using Master Method.
         * Note: Standard mergeSort requires additional space to store temporary arrays
         */

        // Get the size of left and right subarrays
        int n1 = mid - start + 1;
        int n2 = end - mid;

        // Create temporary arrays to store elements
        int[] leftArr = new int[n1]; 
        int[] rightArr = new int[n2];

        // Copy the elements to left and right subarrays
        for (int i = 0; i < n1; i++) {
            leftArr[i] = this.arr[start + i];
        }

        for (int i = 0; i < n2; i++) {
            rightArr[i] = this.arr[mid + 1 + i];
        }

        // Initialise 3 pointers - left for the first element of the left subarray, 
        // right for the first element of right subarray and cur for current position of this.arr
        int left = 0;
        int right = 0;
        int cur = start;

        // Keep looping while we haven't reached the end of either subarray
        while (left < n1 && right < n2) {
            // Compare the first element of both subarrays. Set the current element of this.arr to the smaller element.
            // Then increment the pointer of the subarray whose first element was used as well as the current element pointer
            if (leftArr[left] <= rightArr[right]) {
                this.arr[cur] = leftArr[left];
                left++;
            } else {
                this.arr[cur] = rightArr[right];
                right++;
            }
            cur++;
        }

        // Copy the remaining elements of left subarray 
        while (left < n1) {
            this.arr[cur] = leftArr[left];
            left++;
            cur++;
        }

        // Copy the remaining elements of right subarray
        while (right < n2) {
            this.arr[cur] = rightArr[right];
            right++;
            cur++;
        }
    }

    private int qSortPartition(int start, int end) {
        /*
         * Standard implementation of Partition subroutine used in quickSort.
         * Complexity of O(nlogn) for best case and average case, O(n^2) for worst case
         * Best case when Partition produces subarrays that are balanced. Worst case when Partition produces
         * 1 subarray of length 0 and another of length n - 1. Note that the the complexity for the best case
         * also holds for suboptimal cases such as a 9 to 1 split. As long as the splits have constant
         * proportionality, the complexity will be O(nlogn)
         */

        int pivot = this.arr[end];
        int i = start - 1;
        for (int j = start; j <= end - 1; j++) {
            // Compare each element in subarray (other than pivot element) to pivot.
            // If it is less than or equal to pivot, increment i. Then swap this.arr[i] with this.arr[j]
            // Note: Because of the implementation, if i = j, that is if the 1st element is less than pivot,
            // we simply do nothing rather than swap the element with itself
            if (this.arr[j] <= pivot) {
                i++;
                if (i != j) {
                    this.arr[i] += this.arr[j];
                    this.arr[j] = this.arr[i] - this.arr[j];
                    this.arr[i] -= this.arr[j];
                }
            }
        }
        // After we are done, swap pivot element with the i + 1 element
        // The resulting subarray has 3 partitions in the order: 
        // [<--elements <= pivot--> pivot <--elements > pivot-->]
        this.arr[end] = this.arr[i + 1];
        this.arr[i + 1] = pivot;
        return i + 1;
    }

    private int qSortRandPartition(int start, int end) {
        /*
         * Random implementation of Partition subroutine used in quickSort.
         * Rather than always use the last element as pivot, we randomly choose an element to use as pivot.
         * This random implmentation is used to obtain good performance over all inputs. (Consider if the
         * permutations of the input numbers are not equally likely). Because the pivot element is chosen
         * randomly, we can expect the split of the input array to be well balanced on average.
         */

        int range = start - end + 1;
        int i = (int) (Math.random() * range) + start;
        this.arr[i] += this.arr[end];
        this.arr[end] = this.arr[i] - this.arr[end];
        this.arr[i] -= this.arr[end];
        return qSortPartition(start, end);
    }

    private void maxHeapify(int i, int heapSize) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest;

        if (left < heapSize && this.arr[left] > this.arr[i]) {
            largest = left;
        } else {
            largest = i;
        }

        if (right < heapSize && this.arr[right] > this.arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            this.arr[largest] += this.arr[i];
            this.arr[i] = this.arr[largest] - this.arr[i];
            this.arr[largest] -= this.arr[i];
            maxHeapify(largest, heapSize);
        }
    }

    private int buildMaxHeap() {
        int heapSize = this.arr.length;

        for (int i = Math.floorDiv(this.arr.length, 2) - 1; i >= 0 ; i--) {
            maxHeapify(i, heapSize);
        }

        return heapSize;
    }
}
